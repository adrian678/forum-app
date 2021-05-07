package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import org.springframework.hateoas.RepresentationModel;

import java.util.Collections;
import java.util.List;

public class BoardResponseDto extends RepresentationModel<BoardResponseDto> {
    private String topic;
    private String description;
    private String owner;
    private List<String> rules;
    private Quantity numSubscribers;
    private List<String> moderators;
    private List<PostId> pinnedPosts;

    public BoardResponseDto(String topic, String description, String owner, List<String> rules,
                            Quantity numSubscribers, List<String> moderators, List<PostId> pinnedPosts){
        this.topic = topic;
        this.description = description;
        this.owner = owner;
        this.rules = Collections.unmodifiableList(rules); //TODO defensive copy of List?
        this.numSubscribers = numSubscribers;
        this.moderators = Collections.unmodifiableList(moderators);
        this.pinnedPosts = Collections.unmodifiableList(pinnedPosts);
    }

    public static BoardResponseDto fromBoard(Board board){
        return new BoardResponseDto(board.getName(), board.getDescription(), board.getOwner(),
                board.getRules(), board.getNumSubscribers(), board.getModerators(), board.getPinnedPosts());
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getRules() {
        return rules;
    }

    public Quantity getNumSubscribers() {
        return numSubscribers;
    }

    public List<String> getModerators() {
        return moderators;
    }

    public List<PostId> getPinnedPosts() {
        return pinnedPosts;
    }

}
