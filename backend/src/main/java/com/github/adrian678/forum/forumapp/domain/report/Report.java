package com.github.adrian678.forum.forumapp.domain.report;

import com.github.adrian678.forum.forumapp.domain.user.UserId;

public interface Report {
    //a report may be for an inappropriate comment, post, username, or user bio. Thus only one of these fields need be non-null
    //TODO make subclasses for InappropriateUsername, etc. and set their @Document collection to Reports
    public String getType();

    public ReportId getReportId();

    public String submittedBy();

    public String getReportCategory();

    public String getDescription();

    public boolean isResolved();
}
