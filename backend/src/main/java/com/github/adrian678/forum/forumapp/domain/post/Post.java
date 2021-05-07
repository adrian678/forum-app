package com.github.adrian678.forum.forumapp.domain.post;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;

import static com.github.adrian678.forum.forumapp.domain.Utils.containsWhiteSpace;


/**
 * Post represents a typical submission by a user to a board, containing text.
 *
 * <P>
 *     Contains references to the author of the post, the board that it has been submitted to, a String corresponding to the content of the post,
 *     a String title, an integer point count, an Instant timestamp, and booleans corresponding to archive status and removal status.
 * </P>
 *
 */
@Document
public class Post {
    @Id
    @NonNull
    private PostId pId;            //TODO must add Topic/Group before can finish this class
    @NonNull
    private String boardName;
    @NonNull
    private String author;
    @NonNull
    private String content;
    @NonNull
    private String title;
    @NonNull
    private int points;                 //TODO decide if points should be an Atomic Integer. How to store in db?
    @NonNull
    @Indexed
    private Instant createdAt;     //TODO rename to createdAt?  //TODO make another index on createdAt
    @NonNull
    private boolean archived;

    private boolean isRemoved = false;

    private Post(PostId pId, String author, String boardName, int points, String title, String content, Instant createdAt, boolean isRemoved){
        this.pId = pId;
        this.boardName = boardName;
        this.points = points;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.isRemoved = false;
    }

    /**
     *
     * @param author the User object corresponding to the author that created this post
     * @param boardName the indentification of the board which this post is attached to.
     * @param title the String title of the post
     * @param content the String representing the content of the post
     * @return an instance of Post
     */
    public static Post createPost(String author, String boardName, String title, String content){
        //TODO should these use domain objects instead of Strings/ids
        if(null == author){
            throw new IllegalArgumentException("Invalid author provided to createPost call");
        }
        if(null == boardName){
            throw new IllegalArgumentException("Invalid board provided to createPost call");
        }

        if(null == title || containsWhiteSpace(title)){
            throw new IllegalArgumentException("Invalid title provided to createPost call");
        }
        return new Post(PostId.randomId(), author, boardName, 1, title, content, Instant.now(), false);
    }

    /**
     *
     * @return the identification of this post
     */
    public PostId getpId() {
        return pId;
    }

    /**
     *
     * @return the identity of the author of this post
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return the identity of the board name of this post
     */
    public String getBoardName(){
        return boardName;
    }

    /**
     *
     * @return the content of this post
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @return the integer point count of this post
     */
    public int getPoints() {
        return points;
    }

    /**
     *
     * @return the Instant creation timestamp of this post
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @return whether this post is removed
     */
    public boolean isRemoved() {
        return isRemoved;
    }

    /**
     *
     * @return whether this post is archived
     */
    public boolean isArchived() {
        return archived;
    }

    /**
     *
     * @return the title of this post
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return the creation timestamp of this post
     */
    public Instant createdAt() {
        return createdAt;
    }

    //TODO consider editing a post use case
    /**
     *
     * @param title
     */
    public void editTitle(String title) {
        this.title = title;
    }

    /**
     * Archives the post so that no edits or new comments may be applied to it.
     */
    public void archive(){
//        TODO should there be a method to 'unarchive' a Post?
        this.archived = true;
    }

    /**
     *
     * @param removed whether the post has been removed.
     */
    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }
}
