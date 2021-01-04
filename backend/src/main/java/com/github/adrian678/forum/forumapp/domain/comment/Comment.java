package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Comment {

    @EmbeddedId
    @NonNull
    private CommentId cid;
    @JoinColumn //TODO check if query for a Comment returns the Post as well
    @ManyToOne(cascade = CascadeType.ALL)
    private Post parentPost;

    @ManyToOne(cascade = CascadeType.ALL) //TODO check correct cascade type?
    @JoinColumn
    private Comment parentComment;
    @JoinColumn     //TODO if I use join columns, am I now placing a burden on the ORM mapper?
    UserId author;
    @NonNull
    String content;
    @NonNull
    private Date timestamp;
    @NonNull
    private int points;

    public Comment(Post parentPost, Comment parentComment, UserId author, String content){
        this.points = 1;
        this.timestamp = new Date(System.currentTimeMillis());
        this.parentPost = parentPost;
        this.parentComment = parentComment;
        this.author = author;
        this.content = content;
    }

    public Post getParentPost(){
        return parentPost;
    }

    public Comment getParentComment(){
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