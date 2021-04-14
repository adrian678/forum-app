package com.github.adrian678.forum.forumapp.domain.user;

import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

public class UserResponseDto extends RepresentationModel<UserResponseDto> {
    private String username;
    private Instant createdOn;


    public UserResponseDto(String username, String email, Instant createdOn){
        this.username = username;
        this.createdOn = createdOn;
    }

    public static UserResponseDto fromUser(User user){
        return new UserResponseDto(user.getUsername(), user.getEmail(), user.getCreatedOn());
    }

    public String getUsername() {
        return username;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }
}
