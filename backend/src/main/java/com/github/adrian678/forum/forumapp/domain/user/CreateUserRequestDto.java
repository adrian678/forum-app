package com.github.adrian678.forum.forumapp.domain.user;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequestDto {


    private String username;
    private String password;
    private String email;

    @JsonCreator
    public CreateUserRequestDto(@JsonProperty("username") String username,
                                @JsonProperty("password") String password, @JsonProperty("email") String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }



    public String getUsername() {
        return username;
    }

//    public void setUsername(String username) {
//        this.username = username;
//    }

    public String getPassword() {
        return password;
    }

//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getEmail() {
        return email;
    }

//    public void setEmail(String email) {
//        this.email = email;
//    }


    @Override
    public String toString() {
        return ("username: " + username +", password: " + password + ", email: " + email);
    }
}
