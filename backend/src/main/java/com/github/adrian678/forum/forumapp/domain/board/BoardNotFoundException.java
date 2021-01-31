package com.github.adrian678.forum.forumapp.domain.board;

public class BoardNotFoundException extends RuntimeException{
    public BoardNotFoundException(String msg){
        super(msg);
    }

}
