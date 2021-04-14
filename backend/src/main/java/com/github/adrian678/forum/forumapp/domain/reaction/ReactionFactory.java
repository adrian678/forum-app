package com.github.adrian678.forum.forumapp.domain.reaction;

import com.github.adrian678.forum.forumapp.domain.post.PostId;

public class ReactionFactory {

    public static Reaction produceReaction(String reactionType, PostId postId, String username){
        if("like".equalsIgnoreCase(reactionType)){
            return new LikePostReaction(postId, username);
        }
        return null;
    }
}
