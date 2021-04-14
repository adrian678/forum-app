package com.github.adrian678.forum.forumapp.domain.reaction;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PostReactions")
public class LikePostReaction implements Reaction {

    @Id
    PostReactionId postReactionId;
    PostId postId;
    String username;

    public LikePostReaction(PostId postId, String username){
        this.postReactionId = new PostReactionId(postId, username);
        this.postId = postId;
        this.username = username;
    }

    @Override
    public String isType() {
        return "like";
    }

    @Override
    public int isWorth() {
        return 1;
    }

    @Override
    public boolean isPositive() {
        return true;
    }

    @Override
    public boolean isNegative() {
        return false;
    }

    public PostReactionId getPostReactionId() {
        return postReactionId;
    }

    //TODO remove getUsername and getPostId?
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public PostId getPostId() {
        return postId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  LikePostReaction){
            LikePostReaction reaction = (LikePostReaction) obj;
            return reaction.getPostReactionId().equals(postReactionId);
        }
        return false;
    }
}
