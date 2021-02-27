package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;

//import java.util.Date;


@Document
public class Post {
    @Id
    @NonNull
    private PostId pId;            //TODO must add Topic/Group before can finish this class
    @NonNull
    private String boardName;
    @NonNull
    private UserId author;
    @NonNull
    private String content;
    @NonNull
    private String title;
    @NonNull
    private int points;                 //TODO decide if points should be an Atomic Integer. How to store in db?
    @NonNull
    private Instant createdAt;     //TODO rename to createdAt?
    @NonNull
    private boolean archived;

    private Post(PostId pId, UserId author, String boardName, int points, String title, String content, Instant timestamp){
        this.pId = pId;
        this.boardName = boardName;
        this.points = points;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = timestamp;
    }


    public PostId getpId() {
        return pId;
    }

    public UserId getAuthor() {
        return author;
    }

    public String getBoardName(){
        return boardName;
    }

    public String getContent() {
        return content;
    }

    public static Post createPost(UserId author, String boardName, String title, String content){
        return new Post(PostId.randomId(), author, boardName, 1, title, content, Instant.now());
    }

    public int getPoints() {
        return points;
    }

//    public void upVote(){
//        points = points.incrementByN(1);
//    }

//    public void downVote(){
//        points = points.decrementByN(1);
//    }

    public String getTitle() {
        return title;
    }

    public void editTitle(String title) {
        this.title = title;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void archive(){
//        TODO should there be a method to 'unarchive' a Post?
        this.archived = true;
    }

    public boolean isArchived() {
        return archived;
    }
}
