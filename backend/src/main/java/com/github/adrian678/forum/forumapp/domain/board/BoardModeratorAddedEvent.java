package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;
import com.github.adrian678.forum.forumapp.domain.user.User;

import java.time.Instant;

public class BoardModeratorAddedEvent extends DomainEvent {
    String eventType= "moderator added";
    String details;
    Instant occurredAt;
    EventId identity;
    Board board;
    User newModerator;

    public BoardModeratorAddedEvent(Object source, Instant occurredAt, EventId identity,Board board, User newModerator){
        super(source);
        this.details = "moderator \'" + newModerator.getId() + "\' added to board: \'" + board.getTopic();
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.board = board;
        this.newModerator =  newModerator;
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

    public User getNewModerator() {
        return newModerator;
    }
}
