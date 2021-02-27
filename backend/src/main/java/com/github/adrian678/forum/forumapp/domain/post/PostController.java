package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.reaction.*;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import com.github.adrian678.forum.forumapp.domain.user.UserRepository;
import com.github.adrian678.forum.forumapp.domain.user.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    PostModelAssembler postModelAssembler;
    @Autowired
    PostReactionRepository postReactionRepository;

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

    @PostMapping("/posts")
    public ResponseEntity<?> create(@RequestBody PostCreationRequestDto dto){
        Post newPost = Post.createPost(new UserId(UUID.fromString(dto.getAuthor())),
                dto.getBoardName(), dto.getTitle(), dto.getContent());
        PostResponseDto model = postModelAssembler.toModel(postRepository.save(newPost));
        return  ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/posts/:postId/reactions")
    public ResponseEntity<EntityModel<Reaction>> react(@RequestBody ReactionCreationDto dto){
        //TODO look into factory patterns for this.
        if(dto.getType() == "like"){
            LikePostReaction reaction = new LikePostReaction(PostId.fromString(dto.getPostId()), UserId.fromString(dto.getUserId()));
            EntityModel<Reaction> model = EntityModel.of(postReactionRepository.save(reaction));
            return ResponseEntity.ok(model);
        }
        return ResponseEntity.unprocessableEntity().body(null); //TODO check whether null is acceptable return here
    }


}
