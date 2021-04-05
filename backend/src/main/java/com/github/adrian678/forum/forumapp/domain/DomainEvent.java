package com.github.adrian678.forum.forumapp.domain;

import org.springframework.context.ApplicationEvent;

import java.time.Instant;

public abstract class DomainEvent extends ApplicationEvent {


    public DomainEvent(Object source) {
        super(source);
    }

    public String getEventType() {
        return "abstract";
    }

    public String getDetails() {
        return "abstract";
    }

    public Instant occurredAt() {
        return Instant.now();
    }

    public EventId getIdentity() {
        return EventId.randomId();
    }


}
