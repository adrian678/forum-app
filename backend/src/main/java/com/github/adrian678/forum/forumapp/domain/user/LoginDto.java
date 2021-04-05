package com.github.adrian678.forum.forumapp.domain.user;

import java.util.ArrayList;
import java.util.List;

public class LoginDto {

    private String username;
    private String password;
    private List<String> grantedAuthorities = new ArrayList<String>();

    public LoginDto(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getGrantedAuthorities(){
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(List<String> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }
}
