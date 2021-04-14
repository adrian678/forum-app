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

    private List<UserId> blockedUsers;

    private List<String> subscribedBoards;

    private List<String> authorities;

    private List<UserId> followedUsers;

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
                 List<String> authorities, List<UserId> followedUsers, List<Ban> bans){
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
        return new User(UserId.getInstance(), username, password, email, Instant.now(),true, true,
                true, true,true, new ArrayList<PostId>(),
                new ArrayList<CommentId>(), new ArrayList<UserId>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<UserId>(), new ArrayList<Ban>());
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

    public void blockUser(User userToBlock){
        if(null == userToBlock){
            return;
        }
        blockedUsers.add(userToBlock.getId());
        //TODO change ouutput type to something that can identify success or failure, like a boolean
        //TODO check that the list can handle a new insertion
    }

    public void followUser(User user){
        followedUsers.add(user.getId());
    }


    public void addSubscription(String boardName){
        subscribedBoards.add(boardName);
        //TODO decide on the return type of this. Perhaps return a copy of the list of subscriptions on success?
        //TODO consider changing subscription list to a concurrent list
    }

    public List<PostId> getSavedPosts() {
        return Collections.unmodifiableList(savedPosts);
    }

    public boolean hasAuthority(String authority){
        return authority.contains(authority);
    }

    public void addRole(String role){ //TODO change return type to boolean
        authorities.add(role);
    }

    public void removeRole(String role){
        authorities.remove(role);
    }

    public List<UserId> getFollowedUsers() {
        return followedUsers;   //TODO return defensive copy?
    }

    public List<Ban> getBans() {
        return bans;
    }

    public void addBan(Ban ban){
        bans.add(ban); //TODO are bans immmutable?
        //TODO change return type to boolean
    }

    public void removeBan(Ban ban){
        bans.remove(ban);
        //TODO change return type to boolean
    }

    public void deactivateBan(Ban ban){
        for(Ban oldBan : bans){
            if(oldBan.equals(ban)){
                oldBan.deactivate();
            }
        }
    }

    public boolean isBannedFromBoard(String boardName){
        return !bans.stream().filter(ban->ban.isActive() && ban.getBoardName().equals(boardName)).collect(Collectors.toList()).isEmpty();
    }

    public Ban findBanById(UUID uuid){
        return bans.stream().filter(ban->ban.getUuid().equals(uuid)).findFirst().get();
    }
}
