package com.github.adrian678.forum.forumapp.domain.report;

import com.github.adrian678.forum.forumapp.domain.DomainEvent;
import com.github.adrian678.forum.forumapp.domain.EventId;

import java.time.Instant;

public class ReportCreatedEvent extends DomainEvent {
    String eventType = "report created";
    String details;
    Instant occurredAt;
    EventId identity;
    Report report;

    //TODO refactor
    public ReportCreatedEvent(Object source, Instant occurredAt, EventId identity, Report report){
        super(source);
        this.details = " report of category " + report.getType() + "created with ID" + report.getReportId();
        this.occurredAt = occurredAt;
        this.identity = identity;
        this.report = report;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getDetails() {
        return details;
    }

    //TODO create getter method for occuredAt, for Jackson serializations
    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    @Override
    public EventId getIdentity() {
        return identity;
    }

    public Report getReport() {
        return report;
    }
}
