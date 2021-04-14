package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.PostId;

public class CreateCommentRequestDto {
    PostId postId;
    CommentId parentCommentId;
    String content;


    public CreateCommentRequestDto(PostId postId, CommentId parentCommentId, String content){
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public PostId getPostId() {
        return postId;
    }

    public CommentId getParentCommentId() {
        return parentCommentId;
    }

    public String getContent() {
        return content;
    }
}
