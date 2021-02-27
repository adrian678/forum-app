package com.github.adrian678.forum.forumapp.domain.reaction;

import com.github.adrian678.forum.forumapp.domain.comment.CommentId;
import com.github.adrian678.forum.forumapp.domain.user.UserId;

public interface Reaction {

    String isType();
    int isWorth();
    boolean isPositive();
    boolean isNegative();

}
