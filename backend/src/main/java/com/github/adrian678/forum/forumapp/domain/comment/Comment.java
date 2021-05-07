package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;

/**
 * This class represents Comment which must correspond to a board and post
 * <p>
 *     object containing a String message, a reference to the author id, a timestamp, a reference to the corresponding board, and an integer point count.
 * </p>
 */
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

    /**
     *
     * @param post The post to which this comment is anchored
     * @param parentComment nonnull reference ff this comment is in reply to another comment
     * @param author The user who has created the comment
     * @param content The String message contained in the comment
     * @return a new instance of <code>Comment</code>
     */
    public static Comment create(Post post, CommentId parentComment, User author, String content){
        if(null == post){
            throw new IllegalArgumentException("null reference provided as post argument to Comment.create()");
        }
        if(null == author){
            throw new IllegalArgumentException("null reference provided as author argument to Comment.create()");
        }
        return new Comment(CommentId.randomId(), post.getpId(), parentComment, author.getUsername(),
                content, Instant.now(), 1, post.getBoardName(), false);
    }

    /**
     *
     * @return the identification of the post to which this comment is attached/related
     */
    public PostId getParentPostId(){
        return parentPostId;
    }

    /**
     *
     * @return the identification for this specific comment
     */
    public CommentId getCid(){
        return cid;
    }

    /**
     *
     * @return the identification of the other comment which this comment is replying to
     */
    public CommentId getParentCommentId(){
        return parentCommentId;
    }

    /**
     *
     * @return the identification of the author of this comment
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return the String message which this comment contains
     */
    public String getContent() {
        return content;
    }

//TODO consider use case
//    public void editContent(String content) {
//        this.content = content;
//    }

    /**
     *
     * @return the time instant which this comment was created at
     */
    public Instant createdAt() {
        return createdAt;
    }

    /**
     *
     * @return the integer point count of this comment
     */
    public int getPoints() {
        return points;
    }

    /**
     *
     * @return the identification of the board which the parent post is tied to
     */
    public String getBoardName() {
        return boardName;
    }

    /**
     *
     * @return boolean indicating whether the comment has been removed
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     *
     * @param removed the new boolean state indicating whether this comment has been removed
     */
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