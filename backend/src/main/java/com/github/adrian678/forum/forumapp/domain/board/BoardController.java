package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class BoardController {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardModelAssembler boardModelAssembler;

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
        //TODO check that user has permissions or is not banned.
        //TODO check that a board by that name does not already exist.
        Board board = Board.createNewBoard(dto.getTopic(), dto.getDescription(), UserId.fromString(dto.getOwner()), dto.getRules());
        BoardResponseDto model = boardModelAssembler.toModel(boardRepository.save(board));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    //TODO implement removal or deletion of board. Only admin should be able to perform this action.

}
