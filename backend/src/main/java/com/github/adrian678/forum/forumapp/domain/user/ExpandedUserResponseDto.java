package com.github.adrian678.forum.forumapp.domain.user;

import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ExpandedUserResponseDto extends RepresentationModel<ExpandedUserResponseDto> {

    private UserId uid; //TODO consider flattening this to uuid

    private String username;

    private String email;

    private Instant createdOn;

    private List<UserId> blockedUsers;

    private List<String> subscribedBoards;

    //administrative fields
    private boolean isActive;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    //TODO flatten bans and include them here

    //TODO does a DTO need multiple constructors?
    public ExpandedUserResponseDto(UserId uid, String username, String email, Instant createdOn, boolean isActive,
                                   boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired,
                                   boolean isEnabled, List<UserId> blockedUsers, List<String> subscribedBoards){

        this.uid = uid;
        this.username = username;
        this.email = email;
        this.createdOn = createdOn;
        this.isActive = isActive;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.blockedUsers = new ArrayList<>(blockedUsers);
        this.subscribedBoards = new ArrayList<>(subscribedBoards);
    }

    //

    public UserId getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
