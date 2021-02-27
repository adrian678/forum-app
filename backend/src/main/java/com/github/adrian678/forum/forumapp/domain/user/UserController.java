package com.github.adrian678.forum.forumapp.domain.user;

import com.github.adrian678.forum.forumapp.domain.board.Board;
import com.github.adrian678.forum.forumapp.domain.board.BoardNotFoundException;
import com.github.adrian678.forum.forumapp.domain.board.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BoardRepository boardRepository;

    //TODO make a bean fromString password encoder
//    BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value={"/users/{username}"})
    public EntityModel<?> one(@PathVariable String username){
        User user = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        UserResponseDto responseDto = modelMapper.map(user, UserResponseDto.class);
        return EntityModel.of(responseDto);
//        return userModelAssembler.toModel(responseDto);
    }

    @GetMapping(value={"/users", "/users/"})
    public CollectionModel<EntityModel<User>> all(){
        //TODO limit the users provided. Only an admin should be able to see all users. A regular user should only see himself or herself
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @PostMapping(value={"/users", "/users/"})
    public EntityModel<User> createUser(@RequestBody CreateUserRequestDto userDto){ //TODO Spring tries to convert json request body into User instead fromString UserDto
        //TODO change UserDto to UserCreationDto
        User user = User.createNewUser(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
//        User user = User.createNewUser(username, password, email);
        return EntityModel.of(userRepository.save(user));   //TODO use the ModelAssembler
    }


    //block user  /users/userId/block
    //TODO consider changing api to have principal related actions and path follow the form /me/etc. Like /me/blockedUsers
    @PostMapping(value={"/block_users"})
    public ResponseEntity<?> blockUser(@RequestParam String username, Authentication authentication){
        User userToBlock = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        if(null == authentication){
            throw new RuntimeException("user must be authenticated");
            //TODO consider stopping and returning a 403 Forbidden status code instead
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User principal = userRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername()).get();
        principal.blockUser(userToBlock.getId()); //TODO should the domain action operate on IDs or the whole domain object (like blockUser(user))
        userRepository.save(principal);
        return ResponseEntity.ok().body(userModelAssembler.toModel(userToBlock)); //TODO send back a DTO, not the actual object
    }
    //friend user /users/userId/friend
    //users/userId/follow

    //TODO wrap in a response
    @PostMapping("/users/subscriptions")
    public EntityModel<User> subscribe(@RequestParam String boardName, Authentication authentication){ //TODO Spring tries to convert json request body into User instead fromString UserDto
        if(null == authentication){
            //TODO return resource forbidden response
        }
        //Check that board exists
        Board board = boardRepository.findById(boardName).orElseThrow(() -> new BoardNotFoundException("No such board exists"));
        User authenticatedUser = (User) userService.loadUserByUsername(authentication.getName());
        authenticatedUser.addSubscription(boardName);
        return EntityModel.of(userRepository.save(authenticatedUser));   //TODO use the ModelAssembler
    }
}
