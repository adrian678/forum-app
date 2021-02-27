package com.github.adrian678.forum.forumapp.domain.user;

import org.springframework.hateoas.RepresentationModel;

public class UserResponseDto extends RepresentationModel<UserResponseDto> {
    private String username;
    private String email;

    public UserResponseDto(String username, String email){
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
