package com.github.adrian678.forum.forumapp.domain.user;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

public interface Ban extends Serializable {

    Instant enactedOn(); //TODO see pros and cons of using Date vs long or other time format
    boolean isActive();
    void deactivate();

    //TODO Users should have a list of bans. Perhaps Bans should be incorporated into user package?
}
