package com.github.adrian678.forum.forumapp.domain.user;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;

import java.time.Instant;

public class BanDeactivatedEvent extends DomainEvent {
    String eventType = "ban deactivated";
    String details;
    Instant occurredAt;
    EventId identity;
    Ban ban;


    public BanDeactivatedEvent(Object source, Instant occurredAt, EventId identity, Ban ban){
        super(source);
        this.details = "moderator or admin: " + ban.getIssuer() + " has deactivated a ban";
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.ban = ban;
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

    public Ban getBan() {
        return ban;
    }
}
