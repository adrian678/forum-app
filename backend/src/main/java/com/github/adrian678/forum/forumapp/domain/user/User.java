package com.github.adrian678.forum.forumapp.domain.user;

import com.github.adrian678.forum.forumapp.domain.comment.CommentId;
import com.github.adrian678.forum.forumapp.domain.post.PostId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Document
public class User implements UserDetails {

    @NonNull
    private UserId uid;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @NonNull
    private Date acc_creation_date;

    private List<PostId> savedPosts;

    private List<CommentId> savedComments;

    //user details specific fields
    private boolean isActive;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    @PersistenceConstructor
    private User(UserId uid, String username, String password, String email, Date acc_creation_date, boolean isActive,
                 boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled, List<PostId> savedPosts, List<CommentId> savedComments){
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.acc_creation_date = acc_creation_date; //TODO check if Date is immutable
        this.isActive = isActive;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.savedPosts = new ArrayList<>(savedPosts);
        this.savedComments = new ArrayList<>(savedComments);
    }

    public static User createNewUser(String username, String password, String email){
        return new User(UserId.getInstance(), username, password, email, new Date(System.currentTimeMillis()),
                true, true, true, true, true, new ArrayList<PostId>(), new ArrayList<CommentId>());
    }

    //TODO Spring Controller responses only include properties, not fields. Determine if there are other properties
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void saveNewPost(PostId postId){
        savedPosts.add(postId);
    }

    public void removeSavedPost(PostId postId){
        savedPosts.remove(postId);
    }

    public void saveNewComment(CommentId commentId){
        savedComments.add(commentId);
    }

    public void removeSavedComment(CommentId commentId){
        savedComments.remove(commentId);
    }

}
