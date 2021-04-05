package com.github.adrian678.forum.forumapp.domain.user;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

public class UserCreatedEvent extends DomainEvent {
    String eventType = "user created";
    String details;
    Instant occurredAt;
    EventId identity;
    User user;

    public UserCreatedEvent(Object source, Instant occurredAt, EventId identity, User user){
        super(source);
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.user = user;
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
}
