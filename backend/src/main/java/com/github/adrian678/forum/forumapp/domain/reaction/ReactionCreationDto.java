package com.github.adrian678.forum.forumapp.domain.reaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactionCreationDto {
    String reactionType;

    @JsonCreator
    public ReactionCreationDto(@JsonProperty("reactionType") String reactionType){
        this.reactionType = reactionType;
    }

    public String getReactionType() {
        return reactionType;
    }


}
