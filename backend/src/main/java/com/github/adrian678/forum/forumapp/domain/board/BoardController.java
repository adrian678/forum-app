package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.report.BoardReport;
import com.github.adrian678.forum.forumapp.domain.report.CreateReportDto;
import com.github.adrian678.forum.forumapp.domain.report.ReportCreatedEvent;
import com.github.adrian678.forum.forumapp.domain.report.ReportRepository;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserRepository;
import com.github.adrian678.forum.forumapp.identityandaccess.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BoardController {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardModelAssembler boardModelAssembler;
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    ReportRepository reportRepository;

    @GetMapping(value="/boards")
    public CollectionModel<BoardResponseDto> all(){
        List<BoardResponseDto> boardModels = boardRepository.findAll().stream().map(boardModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(boardModels, linkTo(methodOn(BoardController.class).all()).withSelfRel());
    }

    @GetMapping(value="/boards/{boardName}")
    public ResponseEntity<?> one(@PathVariable String boardName){
        Board board = boardRepository.findById(boardName).orElseThrow(()-> new BoardNotFoundException("no such board"));
        BoardResponseDto model = boardModelAssembler.toModel(board);
        model.add(linkTo(methodOn(BoardController.class).all()).withRel("all boards"));
        model.add(Link.of("/boards/{boardName}/reports").withRel("report"));

        //if unauthenticated, no more links necessary
        if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
            return ResponseEntity.ok(model);
        }
        User authenticatedUser = retrieveFullAuthenticatedUser();
        //moderator specific links
        if(board.hasModeratorByName(authenticatedUser.getUsername())){
            model.add(linkTo(methodOn(BoardController.class).replaceRules(boardName, board.getRules())).withRel("replace rules"));
            model.add(Link.of("/boards/{boardName}/moderators").withRel("moderators"));
        }
        return ResponseEntity.ok(model);
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/boards")
    public ResponseEntity<?> create(@RequestBody BoardCreationDto dto){
        //TODO validate boardName - ensure no spaces in provided String
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principalUsername = (String) authentication.getPrincipal();
        List<String> authorities = authentication.getAuthorities().stream().map(auth -> ((GrantedAuthority) auth).toString()).collect(Collectors.toList());
        if(!principalUsername.equals(dto.getOwner()) && !authorities.contains("ROLE_ADMIN")){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User owner = userRepository.findByUsername(dto.getOwner()).orElseThrow(()-> new UsernameNotFoundException("No such user"));
        if(boardRepository.existsById(dto.getTopic())){
            //TODO See if there is a way to return an exception or a reason why the Board failed
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        //TODO one issue: also need to save change to user. User must be subscribed to the new board.
        Board board = Board.createNewBoard(dto.getTopic(), dto.getDescription(), owner.getUsername(), dto.getRules());
        Board savedBoard = boardRepository.save(board);
        publisher.publishEvent(new BoardCreatedEvent(this, Instant.now(), EventId.randomId(), savedBoard));
        BoardResponseDto model = boardModelAssembler.toModel(savedBoard);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PutMapping("/boards/{boardName}/rules")
    public ResponseEntity<?> replaceRules(@RequestParam String boardName, @RequestBody List<String> newRules){
        User admin = retrieveFullAuthenticatedUser();
        Board board = boardRepository.findById(boardName).orElseThrow(()-> new BoardNotFoundException("no such board"));
        if(board.getOwner().equals(admin.getUsername())){
            board.replaceRules(newRules);
            publisher.publishEvent(new BoardRulesReplacedEvent(this, Instant.now(), EventId.randomId(), board, new ArrayList<>(newRules)));
            return ResponseEntity.ok().build(); //TODO find acceptable content to go in response
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/boards/{boardName}/remove")
    public ResponseEntity<?> remove(@PathVariable String boardName){//TODO make dto including reason for removal
        //requesting user must be authenticated admin
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = principal.getAuthorities().stream().map(authority-> authority.getAuthority()).collect(Collectors.toList());
        if(!authorities.contains("ROLE_ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Board board = boardRepository.findById(boardName).orElseThrow(()->new BoardNotFoundException("No such board"));
        board.setRemoved(true); //TODO consider whether removal should be irreversible
        Board savedBoard = boardRepository.save(board);
        publisher.publishEvent(new BoardCreatedEvent(this, Instant.now(), EventId.randomId(), savedBoard));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize(Role.HAS_ROLE_ADMIN_EXPR)
    @DeleteMapping("/boards/{boardName}")
    public ResponseEntity<?> delete(@PathVariable String boardName){
        User admin = retrieveFullAuthenticatedUser();
        Board boardToBeDeleted = boardRepository.findById(boardName).orElseThrow(()-> new BoardNotFoundException("no such board"));
        boardRepository.delete(boardToBeDeleted);
        publisher.publishEvent(new BoardDeletedEvent(this, Instant.now(), EventId.randomId(), boardToBeDeleted, admin));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PutMapping("/boards/{boardName}/owner")
    public ResponseEntity<?> changeOwner(@PathVariable String boardName, @RequestBody String newModerator){
        User authenticatedUser = retrieveFullAuthenticatedUser();
        Board board = boardRepository.findById(boardName).orElseThrow(()->new BoardNotFoundException("No such board"));
        if(board.getOwner().equals(authenticatedUser.getId()) || principalHasRole("ROLE_ADMIN")){
            //TODO check that the new moderator is subscribed to the board
            User user = userRepository.findByUsername(newModerator).orElseThrow(()-> new UsernameNotFoundException("No such user"));
            Board savedBoard = boardRepository.save(board);
            publisher.publishEvent(new BoardModeratorAddedEvent(this, Instant.now(), EventId.randomId(), savedBoard, user));
            BoardResponseDto model = boardModelAssembler.toModel(savedBoard);
            return ResponseEntity.ok(model.getRequiredLink(IanaLinkRelations.SELF).toUri());

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/boards/{boardName}/rules")
    public ResponseEntity<?> addRule(@PathVariable String boardName, @RequestBody String newRule){
        User authenticatedUser = retrieveFullAuthenticatedUser();
        Board board = boardRepository.findById(boardName).orElseThrow(()->new BoardNotFoundException("No such board"));
        //fail is board has been removed
        if(board.isRemoved() || !board.getOwner().equals(authenticatedUser.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        board.addRule(newRule);
        Board savedBoard = boardRepository.save(board);
        publisher.publishEvent(new BoardRuleAddedEvent(this, Instant.now(), EventId.randomId(), savedBoard, newRule));
        BoardResponseDto model = boardModelAssembler.toModel(savedBoard);
        return ResponseEntity.ok(model.getRequiredLink(IanaLinkRelations.SELF).toUri());
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @DeleteMapping("/boards/{boardName}/rules")
    public ResponseEntity<?> removeRule(@PathVariable String boardName, @RequestBody String newRule){
        User authenticatedUser = retrieveFullAuthenticatedUser();
        Board board = boardRepository.findById(boardName).orElseThrow(()->new BoardNotFoundException("No such board"));
        if(board.isRemoved() || !board.getOwner().equals(authenticatedUser.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            //TODO maybe refactor this to throw an exception?
        }
        board.removeRule(newRule);
        Board savedBoard = boardRepository.save(board);
        publisher.publishEvent(new BoardRuleRemovedEvent(this, Instant.now(), EventId.randomId(), savedBoard, newRule));
        BoardResponseDto model = boardModelAssembler.toModel(savedBoard);
        return ResponseEntity.ok(model.getRequiredLink(IanaLinkRelations.SELF).toUri());
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/boards/{boardName}/moderators")
    public ResponseEntity<?> addModerators(@PathVariable String boardName, @RequestBody String newModerator){
        User authenticatedUser = retrieveFullAuthenticatedUser();
        Board board = boardRepository.findById(boardName).orElseThrow(()->new BoardNotFoundException("No such board"));
        if(board.isRemoved()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //succeed if authentication is authorized
        if(board.getOwner().equals(authenticatedUser.getUsername()) || principalHasRole(Role.ROLE_ADMIN)){
            User user = userRepository.findByUsername(newModerator).orElseThrow(()-> new UsernameNotFoundException("No such user"));
            board.changeOwner(user);
            Board savedBoard = boardRepository.save(board);
            publisher.publishEvent(new BoardOwnerChangedEvent(this, Instant.now(), EventId.randomId(), savedBoard, user));
            BoardResponseDto model = boardModelAssembler.toModel(savedBoard);
            return ResponseEntity.ok(model.getRequiredLink(IanaLinkRelations.SELF).toUri());

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PreAuthorize(Role.HAS_ROLE_BASIC_USER_EXPR)
    @PostMapping("/boards/{boardName}/reports")
    public ResponseEntity<?> createReport(@PathVariable String boardName, @RequestBody CreateReportDto reportDto){
        User authenticatedUser = retrieveFullAuthenticatedUser();
        BoardReport report = BoardReport.createNew(boardName,  authenticatedUser, reportDto.getReportCategory(), reportDto.getDescription());
        BoardReport savedReport = reportRepository.save(report);
        publisher.publishEvent(new ReportCreatedEvent(this, Instant.now(), EventId.randomId(), report));
        //TODO provide endpoints for viewing reports against boards specifically to admins
        //TODO create a Response DTO of reports
        return ResponseEntity.ok().build();
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
