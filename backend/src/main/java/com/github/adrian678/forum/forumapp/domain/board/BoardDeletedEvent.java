package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.user.User;

import java.time.Instant;

public class BoardDeletedEvent extends DomainEvent {
    String eventType= "board created";
    String details;
    Instant occurredAt;
    EventId identity;
    Board board;
    User admin;

    public BoardDeletedEvent(Object source, Instant occurredAt, EventId identity,Board board, User admin){
        super(source);
        this.details = "board: \'" + board.getTopic() + "\' created";
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.board = board;
        this.admin = admin;
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

    public User getAdmin() {
        return admin;
    }
}
