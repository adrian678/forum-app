package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.PostRepository;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    CommentModelAssembler commentModelAssembler;


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
        Comment comment = Comment.create(commentDto.getPostId(),
                commentDto.getParentCommentId(), commentDto.getAuthor(), commentDto.getContent());
        EntityModel<Comment> model = EntityModel.of(commentRepository.save(comment));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    //TODO Rename and match this method to a use case, like EditComment. Also reconsider path
    @PatchMapping("/comments") //TODO PATCH or PUT?
    public ResponseEntity<?> modify(@RequestBody CommentResponseDto commentDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //TODO what is the name for an unauthorized access/method exception?
        if(null == authentication){
            throw new RuntimeException("user must be authenticated");
        }
        //TODO validate that user input IDs actually exist
        //check that the comment exists
        Comment comment = commentRepository.findById(commentDto.getCommentId())
                .orElseThrow(()-> new CommentNotFoundException("no such comment exists"));
        //check that the author of the payload is the same as the authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User principal = userRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername()).get();
        if(!commentDto.getAuthor().equals(principal.getId())){ //TODO check that user has admin privileges if different author
            return ResponseEntity.status(403).build();
            //TODO add error/detail
        }
        comment.editContent(commentDto.getContent());
        return ResponseEntity.ok(commentRepository.save(comment)); //TODO check if correct status code
    }

//    @DeleteMapping("/comments/:commentId")
//    public ResponseEntity<?> modify(@PathVariable String commentId, Authentication authentication){
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        User principal = userRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername()).get();
//        Comment comment = commentRepository.findById(CommentId.fromString(commentId))
//                .orElseThrow(()-> new CommentNotFoundException("no such comment"));
//        if(!comment.getAuthor().equals(principal.getId())){ //TODO check that user has admin privileges if different author
//            return ResponseEntity.status(403).build();
//            //TODO add error/detail
//        }
//        //TODO do I want to delete the comment or set a property to stop people from viewing it?
//                //maybe if the user is not an admin, set a removed boolean. If the user is an admin, delete record from db
//
//    }
}
