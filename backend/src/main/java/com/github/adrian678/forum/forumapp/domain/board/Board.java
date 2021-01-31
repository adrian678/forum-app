package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.Date;
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
    public final Date creationDate;
    @NonNull
    private UserId owner;

    private List<UserId> moderators;

    private List<String> rules;

    private List<PostId> pinnedPosts;

    private Board(String topic, String description, UserId owner, Quantity numSubscribers,
                    Date creationDate, List<UserId> moderators, List<String > rules, List<PostId> pinnedPosts){
        this.topic = topic;
        this.description = description;
        this.owner = owner;
        this.numSubscribers = numSubscribers;
        this.creationDate = new Date(creationDate.getTime()); //TODO is defensive programming necessary here and below?
        this.moderators = new ArrayList<>(moderators);
        this.rules = new ArrayList<>(rules);
        this.pinnedPosts = new ArrayList<>(pinnedPosts); //TODO need to set pinned Posts instead of creating new ones
    }

    public static Board createNewBoard(String topic, String description, UserId owner, List<String> rules){
        return new Board(topic, description, owner, Quantity.of(1), new Date(System.currentTimeMillis()), new ArrayList<UserId>(), rules, new ArrayList<>());
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

}
