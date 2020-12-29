package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.user.User;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Date;

@Entity
public class Post {
//    @Id
//    @NonNull
//    private final PostId pid;            //TODO must add Topic/Group before can finish this class
    @NonNull
    private User author;
    @NonNull
    private String content;
    @NonNull
    private String title;
    @NonNull
    private int points;                 //TODO decide if points should be a value object
    @NonNull
    private Date timestamp;
    @ManyToOne
//    @JoinColumn
//    @NonNull
//    private Group group;    //TODO should a post have multiple topics or just one?

//    public Post(User author, String title, String content, Group group){
//        this.pid = new PostId();
//        this.points = PointsCount.of(1);
//        this.author = author;
//        this.title=title;
//        this.content = content;
//        this.group = group;
//        this.timestamp = new Date(System.currentTimeMillis());
//    }


//    public PostId getPid() {
//        return pid;
//    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
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


}
