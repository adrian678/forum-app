package com.github.adrian678.forum.forumapp.domain.report;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation="Reports")
public class PostReport implements Report {
    @Id
    ReportId reportId;

    String submitter;

    PostId postId;

    String reportCategory;

    String description;

    boolean resolved = false;

    private PostReport(ReportId reportId, String submitter, PostId postId, String reportCategory, String description, boolean resolved){
        this.reportId = reportId;
        this.submitter = submitter;
        this.postId = postId;
        this.reportCategory = reportCategory;
        this.description = description;
        this.resolved = resolved;
    }

    public static PostReport createNew(User submitter, PostId postId, String reportCategory, String description){
        return new PostReport(ReportId.randomId(), submitter.getUsername(), postId, reportCategory, description, false);
    }

    @Override
    public String getType() {
        return "post report";
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
