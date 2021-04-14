package com.github.adrian678.forum.forumapp.domain.reaction;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;

import java.io.Serializable;

public class PostReactionId implements Serializable {

    PostId postId;

    String username;

    //a postreaction should only be created from existing

    public PostReactionId(PostId postId, String username){
        this.postId = postId;
        this.username = username;
    }

    public PostId getPostId() {
        return postId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  PostReactionId){
            PostReactionId reactionId = (PostReactionId) obj;
            return reactionId.getPostId().equals(postId) && reactionId.getUsername().equals(username);
        }
        return false;
    }
}
