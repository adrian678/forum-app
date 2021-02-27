package com.github.adrian678.forum.forumapp.domain.reaction;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PostReactions")
public class LikePostReaction implements Reaction {

    @Id
    PostReactionId postReactionId;

    public LikePostReaction(PostId postId, UserId userId){
        this.postReactionId = new PostReactionId(postId, userId);
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
}
