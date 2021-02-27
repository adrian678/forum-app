package com.github.adrian678.forum.forumapp.domain.reaction;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;

import java.io.Serializable;

public class PostReactionId implements Serializable {

    PostId postId;

    UserId userId;

    //a postreaction should only be created from existing

    public PostReactionId(PostId postId, UserId userId){
        this.postId = postId;
        this.userId = userId;
    }

    public PostId getPostId() {
        return postId;
    }

    public UserId getUserId() {
        return userId;
    }
}
