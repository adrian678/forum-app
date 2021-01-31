package com.github.adrian678.forum.forumapp.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserModelAssembler userModelAssembler;

    //TODO make a bean of password encoder
//    BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value={"/users/{username}"})
    public EntityModel<User> one(@PathVariable String username){
        User user = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        return userModelAssembler.toModel(user);
    }

    @GetMapping(value={"/users", "/users/"})
    public CollectionModel<EntityModel<User>> all(){
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @PostMapping(value={"/users", "/users/"})
    public EntityModel<User> createUser(@RequestBody UserDto userDto){ //TODO Spring tries to convert json request body into User instead of UserDto
        User user = User.createNewUser(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
//        User user = User.createNewUser(username, password, email);
        return EntityModel.of(userRepository.save(user));
        //TODO look into using RequestParams instead?
        //TODO look into use @ModelAttribute to convert to UserDto
        //TODO look into the correct way to bind dtos or complex objects as request params
    }

}
