package com.github.adrian678.forum.forumapp.domain.user;

import java.time.Instant;

public class BanUserRequestDto {
    private String username;
    private String boardName;
    Instant effectiveSince;

    //TODO determine how flexible ban end-dates will be.

    //TODO how to handle a duration in a dto?

}
