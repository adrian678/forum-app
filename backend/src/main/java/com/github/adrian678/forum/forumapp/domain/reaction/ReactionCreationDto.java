package com.github.adrian678.forum.forumapp.domain.reaction;

public class ReactionCreationDto {
    String type;
    String userId;
    String postId;

    public ReactionCreationDto(String type, String userId, String postId){
        this.type = type;
        this.userId = userId;
        this.postId = postId;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }


    public String getPostId() {
        return postId;
    }

}
