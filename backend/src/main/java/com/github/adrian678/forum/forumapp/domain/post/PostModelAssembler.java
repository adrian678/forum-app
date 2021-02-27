package com.github.adrian678.forum.forumapp.domain.post;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<Post, PostResponseDto> {
    @Override
    public CollectionModel<PostResponseDto> toCollectionModel(Iterable<? extends Post> entities) {
        return null;
        //TODO fill in correct implementation for all domain entities
    }

    @Override
    public PostResponseDto toModel(Post post) {
        return PostResponseDto.fromPost(post).add(linkTo(methodOn(PostController.class).one(post.getpId().toString())).withSelfRel(),
        linkTo(methodOn(PostController.class).all()).withRel("posts"));
    }
}
