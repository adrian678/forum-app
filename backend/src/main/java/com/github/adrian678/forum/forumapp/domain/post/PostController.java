package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.Utils;
import com.github.adrian678.forum.forumapp.domain.board.Board;
import com.github.adrian678.forum.forumapp.domain.board.BoardNotFoundException;
import com.github.adrian678.forum.forumapp.domain.board.BoardRepository;
import com.github.adrian678.forum.forumapp.domain.reaction.*;
import com.github.adrian678.forum.forumapp.domain.report.CreateReportDto;
import com.github.adrian678.forum.forumapp.domain.report.PostReport;
import com.github.adrian678.forum.forumapp.domain.report.ReportCreatedEvent;
import com.github.adrian678.forum.forumapp.domain.report.ReportRepository;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import com.github.adrian678.forum.forumapp.domain.user.UserRepository;
import com.github.adrian678.forum.forumapp.identityandaccess.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    ApplicationEventPublisher publisher;

    String ONE_HOUR = "max-age=3600";
    Sort DEFAULT_SORT = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));

    //TODO Try to find a way to return multiple DTO types from the same method
    @GetMapping("/posts")
    public CollectionModel<PostResponseDto> all(){
        List<PostResponseDto> posts = postRepository.findAll().stream()
                .map(postModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(posts, linkTo(methodOn(PostController.class).all()).withSelfRel());
    }

    //TODO implement sorting by most recent
//    public CollectionModel<?> pageOfPosts(int pageNumer){
//
//    }

    //TODO refactor to have the postId as a PostId object, not a String
    //TODO apply a function to each model such that the 'likedBy/savedBy' fields are populated
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> one(@PathVariable String postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Post post = postRepository.findById(PostId.fromString(postId)).orElseThrow(() -> new PostNotFoundException("No such post exists"));
        PostResponseDto model = postModelAssembler.toModel(post);
        HttpHeaders headers = new HttpHeaders();
        if(post.isArchived()){
            //if post is archived, allow it to cache and do not query its relationships to the user
            headers.setCacheControl(ONE_HOUR);  //TODO choose a better cache time limit

        }
        return ResponseEntity.ok().headers(headers).body(model);
        //TODO create separate endpoints for querying if a user has reacted to or saved a particular post or comment
//        if(null == authentication){
//            //TODO fix anonymous authentication issue
//            model.setLikedByUser(false);
//            model.setSavedByUser(false);
//        } else {
//            User principal = retrieveFullAuthenticatedUser();
//            Optional<Reaction> reaction = postReactionRepository.findById(new PostReactionId(post.getpId(), principal.getUsername()));
//            //individual posts will display whether they have been reacted to or saved by the User
//            if(reaction.isPresent()){
//                //TODO either rename Positive/Negative Reaction or add more reactions
//                model.setLikedByUser(reaction.get().isPositive());
//            }
//            model.setSavedByUser(principal.getSavedPosts().contains(PostId.fromString(postId)));
//
//        }


//        return ResponseEntity.ok().headers(headers).body(model);
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> delete(@PathVariable String postId){
        //check that authenticated user is author or mod/admin
        User authenticatedUser = retrieveFullAuthenticatedUser();
        Post postToRemove = postRepository.findById(PostId.fromString(postId)).orElseThrow(()-> new PostNotFoundException("No such post"));
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

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/posts")
    public ResponseEntity<?> create(@RequestBody PostCreationRequestDto dto){
        User author = retrieveFullAuthenticatedUser();
        if(author.isBannedFromBoard(dto.getBoardName())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is currently banned from this board");
        }
        Post newPost = Post.createPost(author.getUsername(),dto.getBoardName(), dto.getTitle(), dto.getContent());
        Post savedPost = postRepository.save(newPost);
        publisher.publishEvent(new PostCreatedEvent(this, Instant.now(), EventId.randomId(), savedPost));
        PostResponseDto model = postModelAssembler.toModel(savedPost);
        return  ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PutMapping("/posts/{postId}/reactions")
    public ResponseEntity<?> reactToPost(@PathVariable String postId, @RequestBody ReactionCreationDto dto){
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Reaction newReaction = ReactionFactory.produceReaction(dto.getReactionType(), PostId.fromString(postId), principal);
        if(null == newReaction){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Reaction> oldReaction = postReactionRepository.findById(new PostReactionId(PostId.fromString(postId), principal));
        if(oldReaction.isPresent()){
            postReactionRepository.delete(oldReaction.get());
        }
        Reaction savedReaction = postReactionRepository.save(newReaction);
        publisher.publishEvent(new ReactionCreatedEvent(this, Instant.now(), EventId.randomId(), savedReaction));
        EntityModel<Reaction> model = EntityModel.of(savedReaction);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/users/{username}/posts")
    public CollectionModel<?> getUserPosts(@PathVariable String username){
        User poster = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such user"));
        List<Post> posts = postRepository.findByAuthor(poster.getUsername());
        //TODO need to filter for removed posts
        List<PostResponseDto> dtos = posts.stream().map(postModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(dtos, linkTo(methodOn(PostController.class).all()).withRel("all")); //TODO figure out correct links to add
    }

    @GetMapping("/boards/{boardName}/posts")
    public CollectionModel<?> getBoardPosts(
            @PathVariable String boardName,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "null") String sort){
        PageRequest request = Utils.createPageRequestFromParams(page, limit, sort);
        Page<Post> posts = postRepository.findByBoardName(boardName, request);
        System.out.println("page size?" + posts.getSize());
        System.out.println("total elements" + posts.getTotalElements());
        //TODO need to filter for removed posts
        List<PostResponseDto> dtos = posts.stream().map(PostResponseDto::fromPost).collect(Collectors.toList());
        System.out.println("num dtos " + dtos.size());
        int previousPage = page - 1;
        int nextPage = page + 1;
        CollectionModel<?> response = CollectionModel.of(dtos);
        if(posts.hasPrevious()){
            response.add(linkTo(methodOn(PostController.class).getBoardPosts(boardName, previousPage, limit, sort)).withRel("previous"));
        }
        if(posts.hasNext()){
            response.add(linkTo(methodOn(PostController.class).getBoardPosts(boardName, nextPage, limit, sort)).withRel("next"));
        }
        return response;
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/posts/{postId}/reports")
    public ResponseEntity<?>  createReport(@PathVariable String postId, @RequestBody CreateReportDto reportDto){
        User authenticatedUser = retrieveFullAuthenticatedUser();
        PostReport report = PostReport.createNew(authenticatedUser, PostId.fromString(postId), reportDto.getReportCategory(), reportDto.getDescription());
        PostReport savedReport = reportRepository.save(report);
        publisher.publishEvent(new ReportCreatedEvent(this, Instant.now(), EventId.randomId(), report));
        //TODO provide endpoints for viewing reports against boards specifically to admins
        //TODO create a Response DTO of reports
        return ResponseEntity.ok().build();
    }

    private User retrieveFullAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername((String) authentication.getPrincipal()).orElseThrow(()->new UsernameNotFoundException("No such user"));
    }

    private boolean principalHasRole(String role){
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if(null == principal){
            return false;
        }
        List<String> authorities = principal.getAuthorities().stream().map(authority-> authority.getAuthority()).collect(Collectors.toList());
        return authorities.contains(role);
    }
}
