package com.github.adrian678.forum.forumapp.domain.user;

import org.bson.codecs.UuidCodec;

import java.time.Instant;
import java.util.UUID;

public class TemporaryBan implements Ban {

    Instant startTime;
    Instant endTime;
    boolean active;
    String issuer;
    String boardName;
    UUID uuid;

    private TemporaryBan(Instant startTime, Instant endTime, boolean active, String issuer, String boardName, UUID uuid){
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = active;
        this.issuer = issuer;
        this.boardName = boardName;
        this.uuid = uuid;
    }

    public static TemporaryBan createNew(Instant startTime, Instant endTime, String issuer, String boardName){
        if(null == startTime || null == endTime || null == issuer || null == boardName){
            throw new IllegalArgumentException("Must be given valid, non-null arguments");
        }
        if(startTime.isAfter(endTime)){
            throw new IllegalArgumentException("Ban cannot end before it begins");
        }
        return new TemporaryBan(startTime, endTime, false, issuer, boardName, UUID.randomUUID());
    }

    @Override
    public Instant getStartTime() {
        return startTime;
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public boolean isActive() {
        return active || Instant.now().isAfter(endTime);
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

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  TemporaryBan){
            return ((TemporaryBan) obj).getUuid().equals(uuid);
        }
        return false;
    }

    public Instant getEndTime(){
        return endTime;
    }
}
