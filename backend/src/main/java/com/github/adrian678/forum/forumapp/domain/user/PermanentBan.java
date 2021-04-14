package com.github.adrian678.forum.forumapp.domain.user;

import java.time.Instant;
import java.util.UUID;

public class PermanentBan implements Ban{
    Instant startTime;
    boolean active;
    String issuer;
    String boardName;
    UUID uuid;
    //TODO add uniquely identifying trait (for equality comparison with other bans)
    //TODO override equals() method

    private PermanentBan(Instant startTime, boolean active, String issuer, String boardName){
        this.startTime = startTime;
        this.active = active;
        this.issuer = issuer;
        this.boardName = boardName;
    }

    public static PermanentBan createNew(Instant startTime, String issuer, String boardName){
        return new PermanentBan(startTime, false, issuer, boardName);
    }

    @Override
    public Instant getStartTime() {
        return startTime;
    }

    @Override
    public boolean isPermanent() {
        return true;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void deactivate() {
        active = false;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getBoardName() {
        return boardName;
    }

}
