package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.user.User;

import java.time.Instant;

public class BoardOwnerChangedEvent extends DomainEvent {
    String eventType= "board owner changed";
    String details;
    Instant occurredAt;
    EventId identity;
    Board board;
    User newOwner;

    public BoardOwnerChangedEvent(Object source, Instant occurredAt, EventId identity,Board board, User newOwner){
        super(source);
        this.details = "moderator \'" + newOwner.getUsername() + "\' added to board: \'" + board.getTopic();
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.board = board;
        this.newOwner =  newOwner;
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

    public Board getBoard() {
        return board;
    }

    public User getNewOwner() {
        return newOwner;
    }
}
