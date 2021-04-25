package com.github.adrian678.forum.forumapp.domain.user;

import java.time.Instant;

public class BanUserRequestDto {
    private Instant startTime;
    private Instant endTime;
    private String bannedUser;
    private String issuerName;
    private String boardName;

    public BanUserRequestDto(Instant startTime, Instant endTime, String bannedUser, String issuerName, String boardName){
        this.startTime = startTime;
        this.endTime = endTime;
        this.bannedUser = bannedUser;
        this.issuerName = issuerName;
        this.boardName = boardName;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public String getBannedUser() {
        return bannedUser;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getIssuerName() {
        return issuerName;
    }
}
