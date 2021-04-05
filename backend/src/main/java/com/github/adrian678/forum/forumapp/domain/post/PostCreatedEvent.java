package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;

import java.time.Instant;

public class PostCreatedEvent extends DomainEvent {

    String eventType = "post created";
    String details;
    Instant occurredAt;
    EventId identity;
    Post post;

    //TODO refactor
    public PostCreatedEvent(Object source, Instant occurredAt, EventId identity, Post post){
        super(source);
        this.details = "Post with ID: " + post.getpId().toString() + "created";
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.post = post;
    }

    //TODO revise whether there should be factory methods for this
//    public static PostCreatedEvent instance(Post post){
//        return new PostCreatedEvent(Instant.now(), EventId.randomId(), post);
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
