package com.github.adrian678.forum.forumapp.domain.report;

import com.github.adrian678.forum.forumapp.domain.comment.CommentId;
import com.github.adrian678.forum.forumapp.domain.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation="Reports")
public class CommentReport implements Report{

    @Id
    ReportId reportId;

    String submitter;

    CommentId commentId;

    String reportCategory;

    String description;

    boolean resolved = false;

    private CommentReport(ReportId reportId, String submitter, CommentId commentId, String reason, String description, boolean resolved){
        this.reportId = reportId;
        this.submitter = submitter;
        this.commentId = commentId;
        this.reportCategory = reason;
        this.description = description;
        this.resolved = resolved;
    }

    public static CommentReport createNew(User submitter, CommentId commentId, String reason, String description){
        return new CommentReport(ReportId.randomId(), submitter.getUsername(), commentId, reason, description, false);
    }

    @Override
    public String getType() {
        return "comment report";
    }

    @Override
    public ReportId getReportId() {
        return reportId;
    }

    @Override
    public String  submittedBy() {
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
