package com.github.adrian678.forum.forumapp.domain.reaction;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;

import java.time.Instant;

public class ReactionCreatedEvent extends DomainEvent {

    String eventType = "post created";
    String details;
    Instant occurredAt;
    EventId identity;
    Reaction reaction;

    public ReactionCreatedEvent(Object source, Instant occurredAt, EventId identity, Reaction reaction){
        super(source);
        this.details = "User " + reaction.getUsername() + " reacted to post with ID: " + reaction.getPostId().toString();
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.reaction = reaction;
    }

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

    public Reaction getReaction() {
        return reaction;
    }
}
