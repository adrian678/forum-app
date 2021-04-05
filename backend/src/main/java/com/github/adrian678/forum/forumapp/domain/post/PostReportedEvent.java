//package com.github.adrian678.forum.forumapp.domain.post;
//
//import com.github.adrian678.forum.forumapp.domain.DomainEvent;
//import com.github.adrian678.forum.forumapp.domain.EventId;
//import com.github.adrian678.forum.forumapp.domain.report.Report;
//import com.github.adrian678.forum.forumapp.domain.report.ReportId;
//
//import java.time.Instant;
//
//public class PostReportedEvent implements DomainEvent {
//
//    String eventType;
//    String details;
//    Instant occurredAt;
//    EventId identity;
//    PostId postId;
//    ReportId reportId;
//    String reportType;
//
//    public PostReportedEvent(String eventType, String details, Instant occurredAt, EventId identity, Post post, Report report){
//        this.eventType = eventType;
//        this.details = details;
//        this.occurredAt = occurredAt;
//        this.identity = identity;
//        this.postId = post.getpId();
//        this.reportId = report.getReportId();
//        this.reportType = report.getType();
//    }
//
//    @Override
//    public String getEventType() {
//        return eventType;
//    }
//
//    @Override
//    public String getDetails() {
//        return details;
//    }
//
//    @Override
//    public Instant occurredAt() {
//        return occurredAt;
//    }
//
//    @Override
//    public EventId getIdentity() {
//        return identity;
//    }
//}
