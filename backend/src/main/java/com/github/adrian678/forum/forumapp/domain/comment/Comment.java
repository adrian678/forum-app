package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.User;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;

@Document
public class Comment {

    @Id
    @NonNull
    private CommentId cid;

    private PostId parentPostId;

    private CommentId parentCommentId;

    String author;
    @NonNull
    String content;
    @NonNull
    private Instant createdAt;
    @NonNull
    private int points;

    private String boardName;

    private boolean removed;

    private Comment(CommentId cid, PostId parentPostId, CommentId parentCommentId, String author,
                    String content, Instant createdAt, int points, String boardName, boolean removed){
        this.cid = cid;
        this.parentPostId = parentPostId;
        this.parentCommentId = parentCommentId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.points = points;
        this.boardName = boardName;
        this.removed = removed;
    }

    public static Comment create(Post post, CommentId parentComment, User author, String content){
        return new Comment(CommentId.randomId(), post.getpId(), parentComment, author.getUsername(),
                content, Instant.now(), 1, post.getBoardName(), false);
    }

    public PostId getParentPostId(){
        return parentPostId;
    }

    public CommentId getCid(){
        return cid;
    }

    public CommentId getParentCommentId(){
        return parentCommentId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void editContent(String content) {
        this.content = content;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public int getPoints() {
        return points;
    }

    public String getBoardName() {
        return boardName;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    //    public void upVote(){
//        points = points.incrementByN(1);
//    } //TODO return new points?
//
//    public void downVote(){
//        points = points.decrementByN(1);
//    }
}