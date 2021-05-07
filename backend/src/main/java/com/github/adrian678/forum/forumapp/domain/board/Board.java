package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Board class represents a categorized, moderated group to which users can submit content to.
 *
 * A board contains a list of privileged users who have access to moderate submissions to the board.
 * Aside from the moderators, the board also retains an owner.
 * A board contains a set of rules dictating guidelines for acceptable submissions and comments to the board.
 *
 */
@Document
public class Board {

    @Id
    @NonNull //TODO is adding NonNull after ID redundant?
    public String name;
    @NonNull
    private final String description;
    @NonNull
    private Quantity numSubscribers;
    @NonNull
    public final Instant createdAt;
    @NonNull
    private String owner;
    @NonNull
    private List<String> moderators;        //TODO consider changing to a Set instead of List
    @NonNull
    private List<String> rules;
    @NonNull
    private List<PostId> pinnedPosts;
    @NonNull
    private boolean removed;

    private Board(String name, String description, String owner, Quantity numSubscribers,
                  Instant createdAt, List<String> moderators, List<String > rules, List<PostId> pinnedPosts, boolean removed){
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.numSubscribers = numSubscribers;
        this.createdAt =  createdAt;//new Date(createdAt.getTime()); //TODO is defensive programming necessary here and below?
        this.moderators = new ArrayList<>(moderators);
        this.rules = new ArrayList<>(rules);
        this.pinnedPosts = new ArrayList<>(pinnedPosts); //TODO need to set pinned Posts instead fromString creating new ones
        this.removed = removed;
    }

    /**
     * Creates an intance of Board
     * @param topic A String name for the board with no spaces
     * @param description A description of the board
     * @param owner the owner/creator of the board
     * @param rules A set of rules describing acceptable content and submissions for the board
     * @return a new instance of Board
     */
    public static Board createNewBoard(String topic, String description, String owner, List<String> rules){
        return new Board(topic, description, owner, Quantity.of(1), Instant.now(), new ArrayList<String>(), rules, new ArrayList<>(), false);
        //TODO replae String owner with User owner?
    }

    //TODO remove the rename, since the name is the ID
//    public void rename(String newName){
//        name = newName;
//    }

    public List<String > getModerators(){
        return Collections.unmodifiableList(moderators);
    }

    public boolean addPinnedPost(Post post){
        return pinnedPosts.add(post.getpId());
    }

    public String getName() {
        return name;
    }

    /**
     *
     * @return the description of the board and its content
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return the non-negative number of users subscribed to the board
     */
    public Quantity getNumSubscribers() {
        return numSubscribers;
    }

    /**
     *
     * @return the time instant that the board was created at
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @return the identity of the board owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @return the String list defining acceptable content of the board
     */
    public List<String> getRules() {
        return rules;
    }

    /**
     *
     * @return the list of posts that moderators or the owner have pinned for easier viewing for clients
     */
    public List<PostId> getPinnedPosts() {
        return pinnedPosts;
    }

    //TODO remove?
//    public boolean containsModerator(User user){
//        return moderators.contains(user.getId());
//    }

    /**
     *
     * @return whether the board has been removed
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     *
     * @param removed
     */
    public void setRemoved(boolean removed){
        this.removed = removed;
    }

    /**
     * Add a subscribed user's identity to the list of moderators
     * @param newModerator A user that has already subscribed to teh board
     * @return true if the user is successfully added as a moderator or is already a moderator. False otherwise
     */
    public boolean addModerator(User newModerator){
        if(removed){
            throw new BoardRemovedException("attempted modification of removed board in addModerator call");
        }
        if(null == newModerator){
            throw new IllegalArgumentException("Null reference provided to addModerator call");
        }
        if(hasModeratorByName(newModerator.getUsername())){
            return true;
        }
        //check is proposed moderator is subscribed to the board
        if(newModerator.hasSubscribedTo(this)){
            return moderators.add(newModerator.getUsername());
        }
        return false;
    }

    /**
     * Replaces the current board owner with another user already subscribed to the board
     * @param newOwner
     * @return
     */
    public boolean changeOwner(User newOwner){
        if(removed){
            throw new BoardRemovedException("attempted modification of removed board in addModerator call");
        }
        if(null == newOwner){
            throw new IllegalArgumentException("Null reference provided as arg to changeOwner");
        }
        //check that owner is subscribed to the board
        if(newOwner.hasSubscribedTo(this)){
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

    /**
     * Replaces the set of rules that submissions to the board must follow
     * @param rules a String list of rules
     */
    public void replaceRules(List<String> rules){
        this.rules = new ArrayList<>(rules);
    }

    /**
     * Checks whether a user with the provided usernams is a moderator of the board
     * @param username the user who may or may not be a moderator
     * @return true if the given user is a moderator, false otherwise
     */
    public boolean hasModeratorByName(String username){
        return moderators.contains(username);
    }

    /**
     * Checks whether a user is a moderator of the board
     * @param user the user who may or may not be a moderator
     * @return true if the given user is a moderator, false otherwise
     */
    public boolean hasModerator(User user){
        return moderators.contains(user.getUsername());
    }
}
