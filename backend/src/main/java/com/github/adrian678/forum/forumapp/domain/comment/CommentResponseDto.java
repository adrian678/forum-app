package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

public class CommentResponseDto extends RepresentationModel<CommentResponseDto> {
    PostId postId;
    CommentId commentId;
    CommentId parentCommentId;
    UserId author;
    String content;
    Instant createdAt;
    int points;
    boolean isLikedByUser;
    boolean isSavedByUser;

    public CommentResponseDto(PostId postId, CommentId commentId, CommentId parentCommentId,
                              UserId author, String content, Instant createdAt, int points){
        this.postId = postId;
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.points = points;
    }

    public static CommentResponseDto fromComment(Comment comment){
        return new CommentResponseDto(
                comment.getParentPostId(), comment.getCid(), comment.getParentCommentId(),
                comment.getAuthor(), comment.getContent(), comment.createdAt(), comment.getPoints()
        );
    }

    public PostId getPostId() {
        return postId;
    }

    public CommentId getCommentId() {
        return commentId;
    }

    public CommentId getParentComment() {
        return parentCommentId;
    }

    public UserId getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
    }

    public void setSavedByUser(boolean savedByUser) {
        isSavedByUser = savedByUser;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public boolean isSavedByUser() {
        return isSavedByUser;
    }
}
