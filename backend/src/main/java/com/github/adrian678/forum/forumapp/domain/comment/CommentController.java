package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.Utils;
import com.github.adrian678.forum.forumapp.domain.board.Board;
import com.github.adrian678.forum.forumapp.domain.board.BoardNotFoundException;
import com.github.adrian678.forum.forumapp.domain.board.BoardRepository;
import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.post.PostNotFoundException;
import com.github.adrian678.forum.forumapp.domain.post.PostRepository;
import com.github.adrian678.forum.forumapp.domain.report.CommentReport;
import com.github.adrian678.forum.forumapp.domain.report.CreateReportDto;
import com.github.adrian678.forum.forumapp.domain.report.ReportCreatedEvent;
import com.github.adrian678.forum.forumapp.domain.report.ReportRepository;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserRepository;
import com.github.adrian678.forum.forumapp.identityandaccess.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentModelAssembler commentModelAssembler;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    ApplicationEventPublisher publisher;


    @GetMapping("/comments")
    public CollectionModel<CommentResponseDto> all(){
        List<CommentResponseDto> comments = commentRepository.findAll().stream()
                .map(commentModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(comments, linkTo(methodOn(CommentController.class).all()).withSelfRel());
    }

    @GetMapping("/comments/{commentId}")
    public CommentResponseDto one(@PathVariable String commentId){
        Comment comment = commentRepository.findById(CommentId.fromString(commentId)).orElseThrow(()-> new CommentNotFoundException("no such comment"));
        return commentModelAssembler.toModel(comment);
    }

    @GetMapping("/posts/{postId}/comments")
    public CollectionModel<?> getPostComments(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "null") String sort) {
        PageRequest request = Utils.createPageRequestFromParams(page, limit, sort);
        Page<Comment> comments = commentRepository.findByParentPostId(PostId.fromString(postId), request);
        System.out.println("comment page had" + comments.getTotalElements());
        List<CommentResponseDto> dtos = comments.stream().map(commentModelAssembler::toModel).collect(Collectors.toList());
        int previousPage = page - 1;
        int nextPage = page + 1;
        CollectionModel<?> response = CollectionModel.of(dtos);
        if(comments.hasPrevious()){
            response.add(linkTo(methodOn(CommentController.class).getPostComments(postId, previousPage, limit, sort)).withRel("previous"));
        }
        if(comments.hasNext()){
            response.add(linkTo(methodOn(CommentController.class).getPostComments(postId, nextPage, limit, sort)).withRel("next"));
        }
        return response;
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/comments")
    public ResponseEntity<?> create(@RequestBody CreateCommentRequestDto commentDto){
        //check if post is open to new comments
        Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new PostNotFoundException("No such post"));
        if(post.isRemoved() | post.isArchived()){
            //produce forbidden response
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        User author = retrieveFullAuthenticatedUser();
        Comment comment = Comment.create(post, commentDto.getParentCommentId(),
                author, commentDto.getContent());
        Comment savedComment = commentRepository.save(comment);
        publisher.publishEvent(new CommentCreatedEvent(this, Instant.now(), EventId.randomId(), savedComment));
        CommentResponseDto dto = commentModelAssembler.toModel(savedComment);
        return ResponseEntity.created(dto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(dto);
    }

//    //TODO Rename and match this method to a use case, like EditComment. Also reconsider path
//    @PatchMapping("/comments") //TODO PATCH or PUT?
//    public ResponseEntity<?> modify(@RequestBody CommentResponseDto commentDto){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        //TODO what is the name for an unauthorized access/method exception?
//        if(null == authentication){
//            throw new RuntimeException("user must be authenticated");
//        }
//        //TODO validate that user input IDs actually exist
//        //check that the comment exists
//        Comment comment = commentRepository.findById(commentDto.getCommentId())
//                .orElseThrow(()-> new CommentNotFoundException("no such comment exists"));
//        //check that the author of the payload is the same as the authenticated user
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        User principal = userRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername()).get();
//        if(!commentDto.getAuthor().equals(principal.getId())){ //TODO check that user has admin privileges if different author
//            return ResponseEntity.status(403).build();
//            //TODO add error/detail
//        }
//        comment.editContent(commentDto.getContent());
//        return ResponseEntity.ok(commentRepository.save(comment)); //TODO check if correct status code
//    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/comments/{commentId}/remove")
    public ResponseEntity<?> remove(@PathVariable String commentId, Authentication authentication){
        User principal = retrieveFullAuthenticatedUser();
        Comment comment = commentRepository.findById(CommentId.fromString(commentId)).orElseThrow(()-> new CommentNotFoundException("no such comment"));
        boolean isAuthorized = false;
        //check if principal is author of the comment
        if(comment.getAuthor().equals(principal.getId()) || principal.hasAuthority(Role.ROLE_ADMIN)){
//            isAuthorized = true;
            comment.setRemoved(true);
            commentRepository.save(comment);
            publisher.publishEvent(new CommentDeletedEvent(this, Instant.now(), EventId.randomId(), comment));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(403).build();
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/comments/{commentId}/reports")
    public ResponseEntity<?> createReport(@PathVariable String commentId, @RequestBody CreateReportDto reportDto){
        User authenticatedUser = retrieveFullAuthenticatedUser();
        CommentReport report = CommentReport.createNew(authenticatedUser, CommentId.fromString(commentId), reportDto.getReportCategory(), reportDto.getDescription());
        CommentReport savedReport = reportRepository.save(report);
        publisher.publishEvent(new ReportCreatedEvent(this, Instant.now(), EventId.randomId(), report));
        //TODO provide endpoints for viewing reports against boards specifically to admins
        //TODO create a Response DTO of reports
        return ResponseEntity.ok().build();
    }

    //TODO see if this can be refactored into a utility class. May be issue with @Autowiring the repository
    private User retrieveFullAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername((String) authentication.getPrincipal()).orElseThrow(()->new UsernameNotFoundException("No such user"));
    }
}
