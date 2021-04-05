package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.board.Board;
import com.github.adrian678.forum.forumapp.domain.board.BoardNotFoundException;
import com.github.adrian678.forum.forumapp.domain.board.BoardRepository;
import com.github.adrian678.forum.forumapp.domain.reaction.*;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import com.github.adrian678.forum.forumapp.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PostController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    PostModelAssembler postModelAssembler;
    @Autowired
    PostReactionRepository postReactionRepository;

    ApplicationEventPublisher publisher;

    String ONE_HOUR = "max-age=3600";

    //TODO Try to find a way to return multiple DTO types from the same method
    @GetMapping("/posts")
    public CollectionModel<PostResponseDto> all(){
        List<PostResponseDto> posts = postRepository.findAll().stream()
                .map(postModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(posts, linkTo(methodOn(PostController.class).all()).withSelfRel());
    }

    //TODO refactor to have the postId as a PostId object, not a String
    @GetMapping("/posts/:postId")
    public ResponseEntity<PostResponseDto> one(@PathVariable String postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Post post = postRepository.findById(PostId.fromString(postId)).orElseThrow(() -> new PostNotFoundException("No such post exists"));
        ResponseEntity<EntityModel<Post>> responseEntity;
        PostResponseDto model = postModelAssembler.toModel(post);
        User principal = userRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername())
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
        Optional<Reaction> reaction = postReactionRepository.findById(new PostReactionId(post.getpId(), principal.getId()));
        if(reaction.isPresent()){
            //TODO either rename Positive/Negative Reaction or add more reactions
            model.setLikedByUser(reaction.get().isPositive());
        }

        if(principal.getSavedPosts().contains(PostId.fromString(postId))){
            model.setSavedByUser(true);
        }
        HttpHeaders headers = new HttpHeaders();
        if(post.isArchived()){
            headers.setCacheControl(ONE_HOUR);
        }
        return ResponseEntity.ok().headers(headers).body(model);
        //TODO test
    }

    @DeleteMapping("/posts/:postId")
    public ResponseEntity<?> delete(@PathVariable String idString){
        //check that authenticated user is author or mod/admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        Post postToRemove = postRepository.findById(PostId.fromString(idString)).orElseThrow(()-> new PostNotFoundException("No such post"));
        List<String> authorities = authenticatedUser.getAuthorities().stream().map(auth -> ((GrantedAuthority) auth).toString()).collect(Collectors.toList());
        boolean authorizedToRemove = false;
        if(postToRemove.getAuthor().equals(authenticatedUser)){
            authorizedToRemove = true;
        } else if(authorities.contains("ROLE_ADMIN")){
            authorizedToRemove = true;
        } else {
            Board board = boardRepository.findById(postToRemove.getBoardName()).orElseThrow(()-> new BoardNotFoundException("No such board"));
            if(board.getModerators().contains(authenticatedUser.getId())){
                authorizedToRemove = true;
            }
        }
        if(authorizedToRemove){
            postToRemove.setRemoved(true);
            postRepository.save(postToRemove);
            publisher.publishEvent(new PostDeletedEvent(this, Instant.now(), EventId.randomId(), postToRemove));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @PostMapping("/posts")
    public ResponseEntity<?> create(@RequestBody PostCreationRequestDto dto){
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if(null == principal){
            //TODO use the isAuthenticated method instead of comparing to null?
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String principalUsername = ((UserDetails) principal.getPrincipal()).getUsername();
        //TODO remove the author from the dto, and get author from auth token
        User author = userRepository.findByUsername(principalUsername).orElseThrow(()->new UsernameNotFoundException("No such user"));
        Post newPost = Post.createPost(author,dto.getBoardName(), dto.getTitle(), dto.getContent());
        Post savedPost = postRepository.save(newPost);
        publisher.publishEvent(new PostCreatedEvent(this, Instant.now(), EventId.randomId(), savedPost));
        PostResponseDto model = postModelAssembler.toModel(savedPost);
        return  ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/posts/:postId/reactions")
    public ResponseEntity<?> reactToPost(@RequestBody ReactionCreationDto dto){
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if(!principal.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //TODO look into factory patterns for this.
        if(dto.getType() == "like"){
            LikePostReaction reaction = new LikePostReaction(PostId.fromString(dto.getPostId()), UserId.fromString(dto.getUserId()));
            EntityModel<Reaction> model = EntityModel.of(postReactionRepository.save(reaction));
            return ResponseEntity.ok(model);
        }
        return ResponseEntity.unprocessableEntity().body(null); //TODO check whether null is acceptable return here
    }

    @GetMapping("/users/:username/posts")
    public CollectionModel<?> getUserPosts(@PathVariable String username){
        User poster = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
        List<Post> posts = postRepository.findByAuthor(poster.getId());
        List<PostResponseDto> dtos = posts.stream().map(postModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(PostController.class).all()).withRel("all")); //TODO figure out correct links to add
    }

    @GetMapping("/boards/:boardName/posts")
    public CollectionModel<?> getBoardPosts(@PathVariable String boardName){
        List<Post> posts = postRepository.findByBoardName(boardName);
        List<PostResponseDto> dtos = posts.stream().map(PostResponseDto::fromPost).collect(Collectors.toList());
        return CollectionModel.of(dtos);    //TODO add links
    }



}
