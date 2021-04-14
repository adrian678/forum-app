package com.github.adrian678.forum.forumapp.domain.user;

import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.board.Board;
import com.github.adrian678.forum.forumapp.domain.board.BoardNotFoundException;
import com.github.adrian678.forum.forumapp.domain.board.BoardRepository;
import com.github.adrian678.forum.forumapp.domain.report.CreateReportDto;
import com.github.adrian678.forum.forumapp.domain.report.ReportCreatedEvent;
import com.github.adrian678.forum.forumapp.domain.report.ReportRepository;
import com.github.adrian678.forum.forumapp.domain.report.UserProfileReport;
import com.github.adrian678.forum.forumapp.identityandaccess.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    ReportRepository reportRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    ApplicationEventPublisher publisher;

    final int PAGE_SIZE = 10;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @GetMapping(value={"/users/{username}"})
    public UserResponseDto one(@PathVariable String username){
        User user = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        return userModelAssembler.toModel(user);
        //TODO incrporate dto into modelAssembler
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @GetMapping(value={"/users", "/users/"})
    public CollectionModel<?> all(){
        List<UserResponseDto> users = new ArrayList<>();
        User authenticatedUser = retrieveFullAuthenticatedUser();
        //only admin can see all users. Other users only see themselves
        if(authenticatedUser.hasAuthority(Role.ROLE_ADMIN)){
            users = userRepository.findAll()
                    .stream()
                    .map(userModelAssembler::toModel)
                    .collect(Collectors.toList());
            return CollectionModel.of(users,
                    linkTo(methodOn(UserController.class).all()).withSelfRel());
        }
        UserResponseDto dto = UserResponseDto.fromUser(authenticatedUser);
        users.add(dto);
        return CollectionModel.of(users);
    }


//    @GetMapping(value={"/users", "/users/"})
//    public CollectionModel<?> getUsersPage(
//              @RequestParam(defaultValue = "1")  int page,
//            @RequestParam(defaultValue = "10") int limit,
//            @RequestParam(defaultValue = "null") String sort){
//        //TODO check whether pageNumber is >= 0
//        int nextPageNum = page + 1;
//        int previousNum = page - 1;
//        Pageable pageRequest = PageRequest.of(page, limit);
//        //TODO check authentication for admin
//
//        Page<User> users = userRepository.findAll(pageRequest);
//        List<UserResponseDto> dtos = users.stream().map(userModelAssembler::toModel).collect(Collectors.toList());
//        CollectionModel<?> response = CollectionModel
//                .of(users,
//                        linkTo(methodOn(UserController.class).getUsersPage(page, limit)).withSelfRel(),
//                        linkTo(methodOn(UserController.class).getUsersPage(previousNum, limit)).withRel("previous"),
//                        linkTo(methodOn(UserController.class).getUsersPage(nextPageNum, limit)).withRel("next"));
//        return response;
//        //TODO check if there is a way to implement caching with page returns
//    }

    @PostMapping("/sign-up")
    public UserResponseDto signUp(@RequestBody CreateUserRequestDto userDto){
        User user = User.createNewUser(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getEmail());
        user.addRole(Role.ROLE_BASIC_USER);
        publisher.publishEvent(new UserCreatedEvent(this, Instant.now(), EventId.randomId(), user));
        return UserResponseDto.fromUser(userRepository.save(user));
    }

    //TODO test
    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("me/blocked-users")
    public ResponseEntity<?> blockUser(@RequestParam String username){
        User userToBlock = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        User authenticatedUser = retrieveFullAuthenticatedUser();
        authenticatedUser.blockUser(userToBlock); //TODO should the domain action operate on IDs or the whole domain object (like blockUser(user))
        userRepository.save(authenticatedUser);
        return ResponseEntity.ok().body(userModelAssembler.toModel(userToBlock)); //TODO send back a DTO, not the actual object
    }

    //TODO test
    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("me/following")
    public ResponseEntity<?> subscribeToUser(@PathVariable String username){
        User userToFollow = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        User authenticatedUser = retrieveFullAuthenticatedUser();
        authenticatedUser.followUser(userToFollow);
        return ResponseEntity.ok().body(userModelAssembler.toModel(userRepository.save(authenticatedUser)));
    }

    //TODO test
    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("users/{userName}/bans")
    public ResponseEntity<?> banUser(@RequestBody BanUserRequestDto requestDto){

        User userToBan = userRepository.findByUsername(requestDto.getBannedUser()).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        User authenticatedUser = retrieveFullAuthenticatedUser();
        //TODO refactor into factory class
        Ban newBan;
        if(null == requestDto.getEndTime()){
            newBan = TemporaryBan.createNew(requestDto.getStartTime(), requestDto.getEndTime(), authenticatedUser.getUsername(), requestDto.getBoardName());
        } else {
            newBan = PermanentBan.createNew(requestDto.getStartTime(), authenticatedUser.getUsername(), requestDto.getBoardName());
        }
        userToBan.addBan(newBan);
        userRepository.save(userToBan);
        publisher.publishEvent(new UserBannedEvent(this, Instant.now(), EventId.randomId(), newBan));
        return ResponseEntity.ok().body(userModelAssembler.toModel(userToBan));
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("users/{username}/bans/{banId}/deactivate")
    public ResponseEntity<?> unbanUser(@PathVariable String username, @PathVariable String banId){
        User userToUnban = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        User authenticatedUser = retrieveFullAuthenticatedUser();
        Ban banToDeactivate = userToUnban.findBanById(UUID.fromString(banId)); //TODO consider exception that may be thrown
        //check if authenticated user has authority to deactivate ban
        Board board = boardRepository.findById(banToDeactivate.getBoardName()).orElseThrow(()-> new BoardNotFoundException("No such board"));
        if(board.hasModeratorByName(authenticatedUser.getUsername()) || authenticatedUser.hasAuthority(Role.ROLE_ADMIN)){
            banToDeactivate.deactivate();
            userRepository.save(userToUnban);
            publisher.publishEvent(new BanDeactivatedEvent(this, Instant.now(), EventId.randomId(), banToDeactivate));
            return ResponseEntity.ok().body(userModelAssembler.toModel(userToUnban)); //TODO return ban instead?
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only an admin or moderator of the board can deactivate the ban on this user");

    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("users/{userName}/reports")
    public ResponseEntity<?> reportUser(@RequestParam String username, @RequestBody CreateReportDto reportDto){
        User reportedUser = userRepository.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("No such user found"));
        User authenticatedUser = retrieveFullAuthenticatedUser();
        UserProfileReport report = UserProfileReport.createNew(authenticatedUser, reportedUser, reportDto.getReportCategory(), reportDto.getDescription());
        UserProfileReport savedReport = reportRepository.save(report);
        publisher.publishEvent(new ReportCreatedEvent(this, Instant.now(), EventId.randomId(), report));
        //TODO provide endpoints for viewing reports against boards specifically to admins
        //TODO create a Response DTO of reports
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
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

    private User retrieveFullAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername((String) authentication.getPrincipal()).orElseThrow(()->new UsernameNotFoundException("No such user"));
    }

    private boolean principalHasRole(String role){
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if(null == principal){
            return false;
        }
        List<String> authorities = principal.getAuthorities().stream().map(authority-> authority.getAuthority()).collect(Collectors.toList());
        return authorities.contains(role);
    }

}
