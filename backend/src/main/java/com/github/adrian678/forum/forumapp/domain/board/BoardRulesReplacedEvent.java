package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;

import java.time.Instant;
import java.util.List;

public class BoardRulesReplacedEvent extends DomainEvent {

    String eventType= "board rules replaced";
    String details;
    Instant occurredAt;
    EventId identity;
    Board board;
    List<String> rules;

    public BoardRulesReplacedEvent(Object source, Instant occurredAt, EventId identity, Board board, List<String> rules){
        super(source);
        this.details = "rule removed from to board: \'" + board.getTopic();
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.board = board;
        this.rules = rules;
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

    public List<String> getRules() {
        return rules;
    }
}
