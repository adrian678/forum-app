package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Document
public class Board {

    @Id
    @NonNull //TODO is adding NonNull after ID redundant?
    public String topic;
    @NonNull
    private final String description;
    @NonNull
    private Quantity numSubscribers;
    @NonNull
    public final Instant createdAt;
    @NonNull
    private String owner;

    private List<String> moderators;        //TODO consider changing to a Set instead of List

    private List<String> rules;

    private List<PostId> pinnedPosts;

    private boolean removed;

    private Board(String topic, String description, String owner, Quantity numSubscribers,
                    Instant createdAt, List<String> moderators, List<String > rules, List<PostId> pinnedPosts, boolean removed){
        this.topic = topic;
        this.description = description;
        this.owner = owner;
        this.numSubscribers = numSubscribers;
        this.createdAt =  createdAt;//new Date(createdAt.getTime()); //TODO is defensive programming necessary here and below?
        this.moderators = new ArrayList<>(moderators);
        this.rules = new ArrayList<>(rules);
        this.pinnedPosts = new ArrayList<>(pinnedPosts); //TODO need to set pinned Posts instead fromString creating new ones
        this.removed = removed;
    }

    public static Board createNewBoard(String topic, String description, String owner, List<String> rules){
        return new Board(topic, description, owner, Quantity.of(1), Instant.now(), new ArrayList<String>(), rules, new ArrayList<>(), false);
    }

    public void rename(String newName){
        topic = newName;
    }

    public List<String > getModerators(){
        return Collections.unmodifiableList(moderators);
    }

    public void addPinnedPost(PostId postId){
        pinnedPosts.add(postId);
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public Quantity getNumSubscribers() {
        return numSubscribers;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getRules() {
        return rules;
    }

    public List<PostId> getPinnedPosts() {
        return pinnedPosts;
    }

    public boolean containsModerator(User user){
        return moderators.contains(user.getId());
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed){
        this.removed = removed;
    }

    public boolean addModerator(User newModerator){
        if(containsModerator(newModerator)){
            return true;
        }
        //check is proposed moderator is subscribed to the board
        if(newModerator.getSubscribedBoards().contains(this)){
            moderators.add(newModerator.getUsername());
            return true;
        }
        return false;
    }

    public boolean changeOwner(User newOwner){
        //check that owner is subscribed to the board
        if(newOwner.getSubscribedBoards().contains(this)){
            owner = newOwner.getUsername();
            return true;
        }
        return false;
    }
    //TODO Board should throw an exception if modifications are attempted when removed is set to true

    public void addRule(String newRule){
        rules.add(newRule);
    }

    public void removeRule(String rule){
        rules.remove(rule);
    }

    public void replaceRules(List<String> rules){
        this.rules = new ArrayList<>(rules);
    }

    public boolean hasModeratorByName(String username){
        return moderators.contains(username);
    }
}
