package com.github.adrian678.forum.forumapp.domain.user;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public interface Ban extends Serializable {

    Instant getStartTime(); //TODO see pros and cons of using Date vs long or other time format
    boolean isPermanent();
    boolean isActive();
    void deactivate();
    String getIssuer();
    String getBoardName();
    UUID getUuid();

    //TODO Users should have a list of bans. Perhaps Bans should be incorporated into user package?
}
