package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;

import java.time.Instant;

public class CreateCommentRequestDto {
    PostId postId;
    CommentId commentId;
    CommentId parentCommentId;
    UserId author;
    String content;


    public CreateCommentRequestDto(PostId postId, CommentId commentId, CommentId parentCommentId,
                                   UserId author, String content){
        this.postId = postId;
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.author = author;
        this.content = content;
    }

    public PostId getPostId() {
        return postId;
    }

    public CommentId getCommentId() {
        return commentId;
    }

    public CommentId getParentCommentId() {
        return parentCommentId;
    }

    public UserId getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
