package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;

import java.time.Instant;

public class PostDeletedEvent extends DomainEvent {

    String eventType = "post deleted";
    String details;
    Instant occurredAt;
    EventId identity;
    PostId postId;

    public PostDeletedEvent(Object source, Instant occurredAt, EventId identity, Post post){
        super(source);
        this.details = "Post with ID: " + post.getpId().toString() + "deleted";
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.postId = post.getpId();
    }

//    public static PostDeletedEvent instance(Post post){
//        return new PostDeletedEvent(Instant.now(), EventId.randomId(), post);
//    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    @Override
    public EventId getIdentity() {
        return identity;
    }
}
