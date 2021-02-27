package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.hateoas.RepresentationModel;

import java.util.Collections;
import java.util.List;

public class BoardResponseDto extends RepresentationModel<BoardResponseDto> {
    private String topic;
    private String description;
    private UserId owner;
    private List<String> rules;
    private Quantity numSubscribers;
    private List<UserId> moderators;
    private List<PostId> pinnedPosts;

    public BoardResponseDto(String topic, String description, UserId owner, List<String> rules,
                            Quantity numSubscribers, List<UserId> moderators, List<PostId> pinnedPosts){
        this.topic = topic;
        this.description = description;
        this.owner = owner;
        this.rules = Collections.unmodifiableList(rules); //TODO defensive copy of List?
        this.numSubscribers = numSubscribers;
        this.moderators = Collections.unmodifiableList(moderators);
        this.pinnedPosts = Collections.unmodifiableList(pinnedPosts);
    }

    public static BoardResponseDto fromBoard(Board board){
        return new BoardResponseDto(board.getTopic(), board.getDescription(), board.getOwner(),
                board.getRules(), board.getNumSubscribers(), board.getModerators(), board.getPinnedPosts());
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserId getOwner() {
        return owner;
    }

    public void setOwner(UserId owner) {
        this.owner = owner;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public Quantity getNumSubscribers() {
        return numSubscribers;
    }

    public void setNumSubscribers(Quantity numSubscribers) {
        this.numSubscribers = numSubscribers;
    }

    public List<UserId> getModerators() {
        return moderators;
    }

    public void setModerators(List<UserId> moderators) {
        this.moderators = moderators;
    }

    public List<PostId> getPinnedPosts() {
        return pinnedPosts;
    }

    public void setPinnedPosts(List<PostId> pinnedPosts) {
        this.pinnedPosts = pinnedPosts;
    }
}
