package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.board.Board;
import com.github.adrian678.forum.forumapp.domain.board.BoardNotFoundException;
import com.github.adrian678.forum.forumapp.domain.board.BoardRepository;
import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.post.PostNotFoundException;
import com.github.adrian678.forum.forumapp.domain.post.PostRepository;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
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
    ApplicationEventPublisher publisher;

    @GetMapping("/comments")
    public CollectionModel<CommentResponseDto> all(){
        List<CommentResponseDto> comments = commentRepository.findAll().stream()
                .map(commentModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(comments, linkTo(methodOn(CommentController.class).all()).withSelfRel());
    }

    @GetMapping("/comments/:commentId")
    public CommentResponseDto one(@PathVariable String commentId){
        Comment comment = commentRepository.findById(CommentId.fromString(commentId)).orElseThrow(()-> new CommentNotFoundException("no such comment"));
        return commentModelAssembler.toModel(comment);
    }

    @PostMapping("/comments")
    public ResponseEntity<?> create(@RequestBody CreateCommentRequestDto commentDto){
        //check if post is open to new comments
        Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new PostNotFoundException("No such post"));
        if(post.isRemoved() | post.isArchived()){
            //produce forbidden response
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        Comment comment = Comment.create(post, commentDto.getParentCommentId(),
                commentDto.getAuthor(), commentDto.getContent());
        Comment savedComment = commentRepository.save(comment);
        publisher.publishEvent(new CommentCreatedEvent(this, Instant.now(), EventId.randomId(), savedComment));
        EntityModel<Comment> model = EntityModel.of(savedComment);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
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

    @DeleteMapping("/comments/:commentId")
    public ResponseEntity<?> modify(@PathVariable String commentId, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User principal = userRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername()).get();
        Comment comment = commentRepository.findById(CommentId.fromString(commentId))
                .orElseThrow(()-> new CommentNotFoundException("no such comment"));
        boolean isAuthorized = false;
        //check if principal is author of the comment
        if(comment.getAuthor().equals(principal.getId())){
            isAuthorized = true;
        } else {
            List<String> authorities = principal.getAuthorities().stream()
                    .map(authority-> authority.getAuthority())
                    .collect(Collectors.toList());
            //check if principal is an admin
            if(authorities.contains("ROLE_ADMIN")){
                isAuthorized = true;
            } else {
                Board board = boardRepository.findById(comment.getBoardName()).orElseThrow(()-> new BoardNotFoundException("No such board found"));
                //check if principal is a moderator
                if(board.containsModerator(principal)){
                    isAuthorized = true;
                }
            }

        }
        if(isAuthorized){
            comment.setRemoved(true);
            commentRepository.save(comment);
            publisher.publishEvent(new CommentDeletedEvent(this, Instant.now(), EventId.randomId(), comment));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(403).build();
    }
}
