package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.board.BoardId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.Date;


@Document
public class Post {
    @Id
    @NonNull
    private PostId pId;            //TODO must add Topic/Group before can finish this class
    @NonNull
    private BoardId boardId;
    @NonNull
    private UserId author;
    @NonNull
    private String content;
    @NonNull
    private String title;
    @NonNull
    private int points;                 //TODO decide if points should be a value object
    @NonNull
    private Date timestamp;
    @NonNull
    private boolean archived;

    private Post(PostId postId, UserId author, BoardId boardId, int points, String title, String content, Date createdAt){
        this.pId = postId;
        this.boardId = boardId;
        this.points = points;
        this.author = author;
        this.title = title;
        this.content = content;
        this.timestamp = new Date(createdAt.getTime());
    }


    public PostId getpId() {
        return pId;
    }

    public UserId getAuthor() {
        return author;
    }

    public BoardId getBoardId(){
        return boardId;
    }

    public String getContent() {
        return content;
    }

    public static Post createPost(UserId author, BoardId boardId, String title, String content){
        return new Post(new PostId(), author, boardId, 1, title, content, new Date(System.currentTimeMillis()));
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void archive(){
//        TODO should there be a method to 'unarchive' a Post?
        this.archived = true;
    }


}
