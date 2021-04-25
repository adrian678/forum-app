package com.github.adrian678.forum.forumapp.domain.user;

import com.github.adrian678.forum.forumapp.domain.comment.CommentId;
import com.github.adrian678.forum.forumapp.domain.post.PostId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.adrian678.forum.forumapp.domain.Utils.containsWhiteSpace;

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
    private Instant createdOn;

    private List<PostId> savedPosts;        //TODO check if there is potential concurrency issue/exploit

    private List<CommentId> savedComments;  //TODO check if there is potential concurrency issue/exploit

    private List<UserId> blockedUsers;      //TODO change to String usernames instead of UserId

    private List<String> subscribedBoards;

    private List<String> authorities;

    private List<String> followedUsers;     //TODO change from userId to String for holding usernames

    private List<Ban> bans;

    //user details specific fields
    private boolean isActive;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;



    @PersistenceConstructor
    private User(UserId uid, String username, String password, String email, Instant createdOn, boolean isActive,
                 boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled,
                 List<PostId> savedPosts, List<CommentId> savedComments, List<UserId> blockedUsers, List<String> subscribedBoards,
                 List<String> authorities, List<String> followedUsers, List<Ban> bans){
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdOn = createdOn;
        this.isActive = isActive;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.savedPosts = new ArrayList<>(savedPosts);
        this.savedComments = new ArrayList<>(savedComments);
        this.blockedUsers = new ArrayList<>(blockedUsers);
        this.subscribedBoards = new ArrayList<>(subscribedBoards);
        this.authorities = new ArrayList<>(authorities);
        this.followedUsers = new ArrayList<>(followedUsers);
        this.bans = new ArrayList<>(bans);
    }

    public static User createNewUser(String username, String password, String email){
        //TODO create validation methods for provided arguments
        if(null == username || containsWhiteSpace(username) || username.isEmpty()){
            throw new IllegalArgumentException("invalid username argument to createNewUser");
        }
        if(null == password || containsWhiteSpace(password) || password.isEmpty()){
            throw new IllegalArgumentException("invalid password argument to createNewUser");
        }

        return new User(UserId.getInstance(), username, password, email, Instant.now(),true, true,
                true, true,true, new ArrayList<PostId>(),
                new ArrayList<CommentId>(), new ArrayList<UserId>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<>());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //TODO check if this populates the authorities field in the object returned by the UserController
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


    public UserId getId() {
        return uid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public UserId getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public List<CommentId> getSavedComments() {
        return savedComments;
    }

    public List<UserId> getBlockedUsers() {
        return blockedUsers;
    }

    public List<String> getSubscribedBoards() {
        return subscribedBoards;
    }

    public boolean isActive(){
        return isActive;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    //TODO should the save/Remove Post and save/remove Comment methods take the whold domain object (Post/Comment) as arg or just ID?
    public void saveNewPost(PostId postId){
        if(null == postId){
            throw new IllegalArgumentException("null reference provided to saveNewPost call");
        }
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

    public boolean blockUser(User userToBlock){
        if(null == userToBlock){
            throw new IllegalArgumentException("null reference provided to block User call");
        }
        if(uid.equals(userToBlock.getUid())){
            throw new IllegalArgumentException("User cannot block self. Same user provided as arugment to blockUser");
        }
        //TODO ensure there are no duplicates; perhaps switch from list to Set?
        return blockedUsers.add(userToBlock.getId());
    }

    //TODO change to use String usernames instead of UserId
    public boolean hasBlockedUser(User user){
        return blockedUsers.contains(user.getUid());
    }

    public boolean followUser(User user){
        if(null == user){
            throw new IllegalArgumentException("null reference provided to followUser call");
        }
        //cannot follow self
        if(uid.equals(user.getUid())){
            throw new IllegalArgumentException("user cannot follow self. Same user provided as argument to followUser");
        }
        return followedUsers.add(user.getUsername());
    }

    public boolean hasFollowedUser(User user){
        if(null == user){
            throw new IllegalArgumentException("Null reference provided to hasFollowedUser method");
        }
        return followedUsers.contains(user.getUsername());
    }


    public boolean addSubscription(String boardName){
        if(boardName == null){
            throw new IllegalArgumentException("null reference provided to addSubscription cal");
        }
        return subscribedBoards.add(boardName);
    }

    public boolean hasSubscribedTo(String boardName){
        if(null == boardName){
            throw new IllegalArgumentException("null reference provided to hasSubscribedTo method");
        }
        return subscribedBoards.contains(boardName);
    }

    public List<PostId> getSavedPosts() {
        return Collections.unmodifiableList(savedPosts);
    }

    public boolean hasAuthority(String authority){
        return authority.contains(authority);
    }

    public boolean addRole(String role){ //TODO change return type to boolean
        if(null == role){
            throw new IllegalArgumentException("null reference provided to addRole call");
        }
        return authorities.add(role);
    }

    public boolean removeRole(String role){
        return authorities.remove(role);
    }

    public List<String> getFollowedUsers() {
        return followedUsers;   //TODO return defensive copy?
    }

    public List<Ban> getBans() {
        return Collections.unmodifiableList(bans);
    }

    public boolean addBan(Ban ban){
        if(null == ban){
            throw new IllegalArgumentException("null reference provided to addBan call");
        }
        return bans.add(ban); //TODO are bans immmutable?
    }

    public boolean removeBan(Ban ban){
        return bans.remove(ban);
    }


    public boolean isBannedFromBoard(String boardName){
        for(Ban ban : bans){
            if(ban.isActive()){
                return true;
            }
        }
        return false;
//        return !bans.stream().filter(ban->ban.isActive() && ban.getBoardName().equals(boardName)).collect(Collectors.toList()).isEmpty();
    }

//    public Ban findBanById(UUID uuid){
//        return bans.stream().filter(ban->ban.getUuid().equals(uuid)).findFirst().get();
//    }


}
