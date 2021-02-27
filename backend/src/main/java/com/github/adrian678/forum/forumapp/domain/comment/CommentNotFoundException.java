package com.github.adrian678.forum.forumapp.domain.comment;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String message){
        super(message);
    }
}
