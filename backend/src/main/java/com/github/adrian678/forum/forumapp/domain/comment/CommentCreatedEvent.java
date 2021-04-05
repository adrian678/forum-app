package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;

import java.time.Instant;

public class CommentCreatedEvent extends DomainEvent {
    String eventType= "comment created";
    String details;
    Instant occurredAt;
    EventId identity;
    Comment comment;

    public CommentCreatedEvent(Object source, Instant occurredAt, EventId identity,Comment comment){
        super(source);
        this.details = "comment with id: \'" + comment.getCid() + "\' created";
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.comment = comment;
    }


    @Override
    public String getEventType() {
        return super.getEventType();
    }

    @Override
    public String getDetails() {
        return super.getDetails();
    }

    @Override
    public Instant occurredAt() {
        return super.occurredAt();
    }

    @Override
    public EventId getIdentity() {
        return super.getIdentity();
    }
}
