package com.github.adrian678.forum.forumapp.domain.report;

import com.github.adrian678.forum.forumapp.domain.comment.CommentId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation="Reports")
public class BrokenBoardRulesReport implements Report{
    @Id
    ReportId reportId;

    String boardName;

    UserId submitter;

    CommentId commentId;

    String description;

    boolean resolved = false;

    private BrokenBoardRulesReport(ReportId reportId, String boardName, UserId submitter, CommentId commentId, String description, boolean resolved){
        this.reportId = reportId;
        this.boardName = boardName;
        this.submitter = submitter;
        this.commentId = commentId;
        this.description = description;
        this.resolved = resolved;
    }

    public BrokenBoardRulesReport createNew(String boardName, UserId submitter, CommentId commentId, String description){
        return new BrokenBoardRulesReport(ReportId.randomId(), boardName,
                submitter, commentId, description, false);
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public ReportId getReportId() {
        return null;
    }

    @Override
    public String getBoardName() {
        return null;
    }

    @Override
    public UserId submittedBy() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isResolved() {
        return false;
    }
}
