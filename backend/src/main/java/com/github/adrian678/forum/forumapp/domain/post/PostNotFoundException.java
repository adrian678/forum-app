package com.github.adrian678.forum.forumapp.domain.post;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message){
        super(message);
    }
}
