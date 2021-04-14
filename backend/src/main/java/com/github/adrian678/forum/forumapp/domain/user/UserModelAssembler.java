package com.github.adrian678.forum.forumapp.domain.user;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, UserResponseDto> {
//    @Override
//    public CollectionModel<UserResponseDto> toCollectionModel(Iterable<? extends User> entities)
//    {
//        return CollectionModel.of(entities)
//    }

    @Override
    public UserResponseDto toModel(User user) {
        System.out.println(user);
        //TODO how to account for pagination + sorting parameters?
        return UserResponseDto.fromUser(user).add(linkTo(methodOn(UserController.class).one(user.getUsername())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }
}
