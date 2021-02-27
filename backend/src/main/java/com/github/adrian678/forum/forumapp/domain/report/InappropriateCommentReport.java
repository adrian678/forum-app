package com.github.adrian678.forum.forumapp.domain.report;

import com.github.adrian678.forum.forumapp.domain.comment.CommentId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation="Reports")
public class InappropriateCommentReport implements Report{

    @Id
    ReportId reportId;

    String boardName;

    UserId submitter;

    CommentId commentId;

    String description;

    boolean resolved = false;

    private InappropriateCommentReport(ReportId reportId, String boardName, UserId submitter, CommentId commentId, String description, boolean resolved){
        this.reportId = reportId;
        this.boardName = boardName;
        this.submitter = submitter;
        this.commentId = commentId;
        this.description = description;
        this.resolved = resolved;
    }

    public InappropriateCommentReport createNew(String boardName, UserId submitter, CommentId commentId, String description){
        return new InappropriateCommentReport(ReportId.randomId(), boardName,
            submitter, commentId, description, false);
    }

    @Override
    public String getType() {
        return "Inappropriate Comment";
    }

    @Override
    public ReportId getReportId() {
        return reportId;
    }

    @Override
    public String getBoardName() {
        return boardName;
    }

    @Override
    public UserId submittedBy() {
        return submitter;
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
