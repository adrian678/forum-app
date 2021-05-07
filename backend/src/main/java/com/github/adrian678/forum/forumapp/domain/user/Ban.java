package com.github.adrian678.forum.forumapp.domain.user;

import com.github.adrian678.forum.forumapp.domain.board.Board;

import java.time.Instant;
import java.util.Objects;

/**
 * An object with an effective time period preventing users from posting to the corresponding board.
 */
public class Ban{
    DateRange dateRange;
    String issuerName;
    String boardName;

    /**
     *
     * @param dateRange the time period for which the Ban is active
     * @param issuerName the identifier for the moderator who issued the ban
     * @param board the identifier for the board that this ban applies to
     */
    public Ban(DateRange dateRange,  String issuerName, Board board){
        this.dateRange = dateRange;
        this.issuerName = issuerName;
        this.boardName = board.getName();
    }

    /**
     *
     * @param issuerName the identifier for the moderator who issued the ban
     * @param board the identifier for the board that this ban applies to
     * @return an instance of Ban
     */
    public static Ban createPermanent(String issuerName, Board board){
        return new Ban(new DateRange(Instant.now(), Instant.MAX), issuerName, board);
    }


    /**
     *
     * @return the DateRange of this instance of Ban
     */
    public DateRange getDateRange() {
        return dateRange;
    }

    /**
     *
     * @return whether the ban is in effect or not
     */
    public boolean isActive() {
        return dateRange.includes(Instant.now());
    }

    /**
     *
     * @return the identifier for the moderator that issued the ban
     */
    public String getIssuerName() {
        return issuerName;
    }

    /**
     *
     * @return the identifier for the board the ban was issued from
     */
    public String getBoardName() {
        return boardName;
    }

    /**
     *
     * @param obj the object to be tested for equality
     * @return true if the values of the object are equal to this object, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Ban){
            Ban otherBan = (Ban) obj;
            return dateRange.equals(otherBan.getDateRange()) && issuerName.equals(otherBan.getIssuerName()) && boardName.equals(otherBan.getBoardName());
        }
        return false;
    }

    /**
     *
     * @return an integer hash of the contents of this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(dateRange, issuerName, boardName);
    }

    @Override
    public String toString() {
        return "issued by: " + issuerName + dateRange.toString();
    }
}

