package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;

import java.time.Instant;

//TODO make events serializable
public class BoardCreatedEvent extends DomainEvent {
    String eventType= "board created";
    String details;
    Instant occurredAt;
    EventId identity;
    Board board;

    public BoardCreatedEvent(Object source, Instant occurredAt, EventId identity,Board board){
        super(source);
        this.details = "board: \'" + board.getTopic() + "\' created";
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.board = board;
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
