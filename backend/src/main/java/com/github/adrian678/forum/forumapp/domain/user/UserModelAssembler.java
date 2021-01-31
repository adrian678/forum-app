package com.github.adrian678.forum.forumapp.domain.user;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities) {
        return null;
    }

    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user, linkTo(methodOn(UserController.class).one(user.getUsername())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }
}
