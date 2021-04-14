package com.github.adrian678.forum.forumapp.domain.report;

import com.github.adrian678.forum.forumapp.domain.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation="Reports")
public class BoardReport implements Report{

    @Id
    ReportId reportId;

    String boardName;

    String submitter;

    String reportCategory;

    String description;

    boolean resolved = false;

    private BoardReport(ReportId reportId, String boardName, String submitter, String reason, String description, boolean resolved){
        this.reportId = reportId;
        this.boardName = boardName;
        this.submitter = submitter;
        this.reportCategory = reason;
        this.description = description;
        this.resolved = resolved;
    }

    public static BoardReport createNew(String boardName, User submitter, String reason, String description){
        return new BoardReport(ReportId.randomId(), boardName, submitter.getUsername(),  reason, description, false);
    }

    @Override
    public String getType() {
        return "board report";
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
