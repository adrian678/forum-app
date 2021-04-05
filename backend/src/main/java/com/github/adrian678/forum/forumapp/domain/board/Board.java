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

    private List<UserId> moderators;        //TODO consider changing to a Set instead of List

    private List<String> rules;

    private List<PostId> pinnedPosts;

    private boolean removed;

    private Board(String topic, String description, User owner, Quantity numSubscribers,
                    Instant createdAt, List<UserId> moderators, List<String > rules, List<PostId> pinnedPosts, boolean removed){
        this.topic = topic;
        this.description = description;
        this.owner = owner.getUsername();
        this.numSubscribers = numSubscribers;
        this.createdAt =  createdAt;//new Date(createdAt.getTime()); //TODO is defensive programming necessary here and below?
        this.moderators = new ArrayList<>(moderators);
        this.rules = new ArrayList<>(rules);
        this.pinnedPosts = new ArrayList<>(pinnedPosts); //TODO need to set pinned Posts instead fromString creating new ones
        this.removed = removed;
    }

    public static Board createNewBoard(String topic, String description, User owner, List<String> rules){
        return new Board(topic, description, owner, Quantity.of(1), Instant.now(), new ArrayList<UserId>(), rules, new ArrayList<>(), false);
    }

    public void rename(String newName){
        topic = newName;
    }

    public List<UserId> getModerators(){
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
            moderators.add(newModerator.getId());
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
}
