package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class BoardController {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardModelAssembler boardModelAssembler;
    @Autowired
    ApplicationEventPublisher publisher;

    @GetMapping("/boards")
    public CollectionModel<BoardResponseDto> all(){
        List<BoardResponseDto> boardModels = boardRepository.findAll().stream().map(boardModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(boardModels, linkTo(methodOn(BoardController.class).all()).withSelfRel());
    }

    @GetMapping("/boards/:boardName")
    public BoardResponseDto one(@PathVariable String boardName){
        Board board = boardRepository.findById(boardName).orElseThrow(()-> new BoardNotFoundException("no such board"));
        return boardModelAssembler.toModel(board);
    }
//TODO implement below
    @PostMapping("/boards")
    public ResponseEntity<?> create(@RequestBody BoardCreationDto dto){
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if(null == principal){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String principalUsername = ( (UserDetails) principal).getUsername();
        List<String> authorities = principal.getAuthorities().stream().map(auth -> ((GrantedAuthority) auth).toString()).collect(Collectors.toList());
        //only an admin can make another user the owner of a board. All other requesting users must make themselves owner
        //TODO create a file in the security package full of constants corresponding to roles
        if(!principalUsername.equals(dto.getOwner()) && !authorities.contains("ROLE_ADMIN")){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User owner = userRepository.findByUsername(dto.getOwner()).orElseThrow(()-> new UsernameNotFoundException("No such user"));
        //TODO check that a board by that name does not already exist.
        if(boardRepository.existsById(dto.getTopic())){
            //TODO See if there is a way to return an exception or a reason why the Board failed
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Board board = Board.createNewBoard(dto.getTopic(), dto.getDescription(), owner, dto.getRules());
        Board savedBoard = boardRepository.save(board);
        publisher.publishEvent(new BoardCreatedEvent(this, Instant.now(), EventId.randomId(), savedBoard));
        BoardResponseDto model = boardModelAssembler.toModel(savedBoard);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/boards/:boardName")
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

    //TODO implement change of ownership of a board
    @PutMapping("//boards/:boardName/owner")
    public ResponseEntity<?> changeOwner(@PathVariable String boardName, @RequestBody String newModerator){
        User authenticatedUser = userRepository.findByUsername(((UserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername()).orElseThrow(()->new UsernameNotFoundException("No such user"));
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

    //TODO implement changing the rules of the board (or maybe adding new rules one by one?

    @PostMapping("/boards/:boardName/moderators")
    public ResponseEntity<?> addModerators(@PathVariable String boardName, @RequestBody String newModerator){
        //check authenticated user has correct authority
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if(null == principal){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User authenticatedUser = userRepository.findByUsername(((UserDetails) principal.getPrincipal()).getUsername()).orElseThrow(()->new UsernameNotFoundException("No such user"));
        Board board = boardRepository.findById(boardName).orElseThrow(()->new BoardNotFoundException("No such board"));
        if(board.getOwner().equals(authenticatedUser.getUsername()) || principalHasRole("ROLE_ADMIN")){
            User user = userRepository.findByUsername(newModerator).orElseThrow(()-> new UsernameNotFoundException("No such user"));
            board.changeOwner(user);
            Board savedBoard = boardRepository.save(board);
            publisher.publishEvent(new BoardOwnerChangedEvent(this, Instant.now(), EventId.randomId(), savedBoard, user));
            BoardResponseDto model = boardModelAssembler.toModel(savedBoard);
            return ResponseEntity.ok(model.getRequiredLink(IanaLinkRelations.SELF).toUri());

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
