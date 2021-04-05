package com.github.adrian678.forum.forumapp.domain.user;

import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.board.Board;
import com.github.adrian678.forum.forumapp.domain.board.BoardNotFoundException;
import com.github.adrian678.forum.forumapp.domain.board.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController implements ApplicationEventPublisherAware {

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

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @GetMapping(value={"/users/{username}"})
    public UserResponseDto one(@PathVariable String username){
        User user = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        return userModelAssembler.toModel(user);
    }

    @GetMapping(value={"/users", "/users/"})
    public CollectionModel<?> all(){
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        List<UserResponseDto> users = new ArrayList<>();
        if(!principal.getAuthorities()
                .stream()
                .map(authority-> authority.getAuthority())
                .collect(Collectors.toList()).contains("ROLE_ADMIN")){

            users = userRepository.findAll()
                    .stream()
                    .map(userModelAssembler::toModel)
                    .collect(Collectors.toList());
            return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
        }
        UserResponseDto dto = UserResponseDto.fromUser((User) principal);
        users.add(dto);
        return CollectionModel.of(users);
    }

    @PostMapping("/sign-up")
    public UserResponseDto signUp(@RequestBody CreateUserRequestDto userDto){
        User user = User.createNewUser(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getEmail());
        publisher.publishEvent(new UserCreatedEvent(this, Instant.now(), EventId.randomId(), user));
        return UserResponseDto.fromUser(userRepository.save(user));
    }

    @PostMapping("me/blocked-users")
    public ResponseEntity<?> blockUser(@RequestParam String username){
        User userToBlock = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = userRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername()).get();
        principal.blockUser(userToBlock.getId()); //TODO should the domain action operate on IDs or the whole domain object (like blockUser(user))
        userRepository.save(principal);
        return ResponseEntity.ok().body(userModelAssembler.toModel(userToBlock)); //TODO send back a DTO, not the actual object
    }

    @PostMapping("me/following")
    public ResponseEntity<?> subscribeToUser(@PathVariable String username){
        User userToFollow = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //TODO check if default DAO implementation puts a UserDetails or String object into Authentication
        User principal = userRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername()).get();
        principal.followUser(userToFollow);
        return ResponseEntity.ok().body(userModelAssembler.toModel(userRepository.save(principal)));
    }

    @PostMapping("users/:userName/bans")
    public ResponseEntity<?> banUser(@RequestParam String username){
        User userToBlock = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = userRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername()).get();
        principal.blockUser(userToBlock.getId()); //TODO should the domain action operate on IDs or the whole domain object (like blockUser(user))
        userRepository.save(principal);
        return ResponseEntity.ok().body(userModelAssembler.toModel(userToBlock)); //TODO send back a DTO, not the actual object
    }

    //TODO change the path.
    @PostMapping("/me/subscriptions")
    public ResponseEntity<?> subscribe(@RequestParam String boardName){ //TODO Spring tries to convert json request body into User instead fromString UserDto
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null == authentication){
            //TODO check if possible to reach here despite SecurityConfig restrictions
        }
        //Check that board exists
        Board board = boardRepository.findById(boardName).orElseThrow(() -> new BoardNotFoundException("No such board exists"));
        //TODO check if the object returnd by default DaoAuthentication is a User object
        User authenticatedUser = (User) userService.loadUserByUsername(authentication.getName());
        authenticatedUser.addSubscription(boardName);
        return ResponseEntity.ok(userModelAssembler.toModel(userRepository.save(authenticatedUser)));
        //TODO need to increase the subscriber count of the board too
    }


}
