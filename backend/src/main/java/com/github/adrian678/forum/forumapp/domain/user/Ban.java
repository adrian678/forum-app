package com.github.adrian678.forum.forumapp.domain.user;

import java.time.Instant;
import java.util.Objects;

public class Ban{
    DateRange dateRange;
    String issuerName;
    String boardName;

    public Ban(DateRange dateRange,  String issuerName, String boardName){
        this.dateRange = dateRange;
        this.issuerName = issuerName;
        this.boardName = boardName;
    }

    public static Ban createPermanent(String issuerName, String boardName){
        return new Ban(new DateRange(Instant.now(), Instant.MAX), issuerName, boardName);
    }


    public DateRange getDateRange() {
        return dateRange;
    }

    public boolean isActive() {
        return dateRange.includes(Instant.now());
    }

    public String getIssuerName() {
        return issuerName;
    }

    public String getBoardName() {
        return boardName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Ban){
            Ban otherBan = (Ban) obj;
            return dateRange.equals(otherBan.getDateRange()) && issuerName.equals(otherBan.getIssuerName()) && boardName.equals(otherBan.getBoardName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateRange, issuerName, boardName);
    }
}

