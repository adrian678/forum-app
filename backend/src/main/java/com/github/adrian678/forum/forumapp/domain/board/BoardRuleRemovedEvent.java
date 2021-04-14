package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;

import java.time.Instant;

public class BoardRuleRemovedEvent extends DomainEvent {

    String eventType= "board rule removed";
    String details;
    Instant occurredAt;
    EventId identity;
    Board board;
    String rule;

    public BoardRuleRemovedEvent(Object source, Instant occurredAt, EventId identity, Board board, String rule){
        super(source);
        this.details = "rule removed from to board: \'" + board.getTopic();
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.board = board;
        this.rule = rule;
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

    public String getRule() {
        return rule;
    }
}
