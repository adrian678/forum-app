package com.github.adrian678.forum.forumapp.domain.report;

import com.github.adrian678.forum.forumapp.domain.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation="Reports")
public class UserProfileReport implements Report {
    @Id
    ReportId reportId;

    String submitter;

    String offender;

    String reportCategory;

    String description;

    boolean resolved = false;

    private UserProfileReport(ReportId reportId, String  submitter, String offender, String reason, String description, boolean resolved){
        this.reportId = reportId;
        this.submitter = submitter;
        this.offender = offender;
        this.reportCategory = reason;
        this.description = description;
        this.resolved = resolved;
    }

    public static UserProfileReport createNew(User submitter, User offender, String reason, String description){
        return new UserProfileReport(ReportId.randomId(), submitter.getUsername(), offender.getUsername(), reason, description, false);
    }

    @Override
    public String getType() {
        return "user profile report";
    }

    @Override
    public ReportId getReportId() {
        return reportId;
    }

    @Override
    public String submittedBy() {
        return submitter;
    }

    @Override
    public String getReportCategory() {
        return reportCategory;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isResolved() {
        return resolved;
    }
}
