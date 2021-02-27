package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
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

    UserId author;
    @NonNull
    String content;
    @NonNull
    private Instant createdAt; //TODO change to Instant from Java 8 DateTIme api
    @NonNull
    private int points;

    private Comment(CommentId cid, PostId parentPostId, CommentId parentComment, UserId author, String content, Instant createdAt, int points){
        this.cid = cid;
        this.parentPostId = parentPostId;
        this.parentCommentId = parentComment;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.points = points;
    }

    //TODO create factory method
    public static Comment create(PostId postId, CommentId parentComment, UserId author, String content){
        return new Comment(CommentId.randomId(), postId, parentComment, author, content, Instant.now(), 1);
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

    public UserId getAuthor() {
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

//    public void upVote(){
//        points = points.incrementByN(1);
//    } //TODO return new points?
//
//    public void downVote(){
//        points = points.decrementByN(1);
//    }
}