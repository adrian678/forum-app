package com.github.adrian678.forum.forumapp.domain.board;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BoardModelAssembler implements RepresentationModelAssembler<Board, BoardResponseDto> {

    @Override
    public CollectionModel<BoardResponseDto> toCollectionModel(Iterable<? extends Board> entities)
    {//TODO implement
        return null;
    }

    @Override
    public BoardResponseDto toModel(Board board) {
        return BoardResponseDto.fromBoard(board)
                .add( linkTo(methodOn(BoardController.class).one(board.getName())).withSelfRel(),
                linkTo(methodOn(BoardController.class).all()).withRel("boards"));
    }

}
