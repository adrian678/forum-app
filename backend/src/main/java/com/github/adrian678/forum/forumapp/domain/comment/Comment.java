package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.Date;

@Document
public class Comment {

    @Id
    @NonNull
    private CommentId cid;

    private PostId parentPost;

    private CommentId parentComment;

    UserId author;
    @NonNull
    String content;
    @NonNull
    private Date timestamp;
    @NonNull
    private int points;

    private Comment(CommentId cid, PostId parentPost, CommentId parentComment, UserId author, String content, Date timestamp, int points){
        this.cid = cid;
        this.parentPost = parentPost;
        this.parentComment = parentComment;
        this.author = author;
        this.content = content;
        this.timestamp = new Date(timestamp.getTime());
        this.points = points;
    }

    //TODO create factory method

    public PostId getParentPost(){
        return parentPost;
    }

    public CommentId getParentComment(){
        return parentComment;
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

    public Date getTimestamp() {
        return timestamp;
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