package com.github.adrian678.forum.forumapp.domain.comment;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CommentModelAssembler implements RepresentationModelAssembler<Comment, CommentResponseDto> {

    @Override
    public CollectionModel<CommentResponseDto> toCollectionModel(Iterable<? extends Comment> entities) {
        return null;
    }

    @Override
    public CommentResponseDto toModel(Comment comment) {
        return CommentResponseDto.fromComment(comment)
                .add( linkTo(methodOn(CommentController.class).one(comment.getCid().toString())).withSelfRel(),
                linkTo(methodOn(CommentController.class).all()).withRel("comments"));
    }
}
