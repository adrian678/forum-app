package com.github.adrian678.forum.forumapp.domain.user;


import java.time.Instant;
import java.util.Objects;

public class DateRange implements Comparable<DateRange> {

    Instant start;
    Instant end;

    public DateRange(Instant start, Instant end){
        this.start = start;
        this.end = end;
    }

    public static DateRange upTo(Instant end){
        return new DateRange(Instant.MIN, end);
    }

    public static DateRange startingFrom(Instant start){
        return new DateRange(start, Instant.MAX);
    }

    public boolean includes(Instant instant){
        return start.isBefore(instant) && end.isAfter(instant);
    }

    public boolean includes(DateRange range){
        return start.isBefore(range.getStart()) && end.isAfter(range.getEnd());
    }

    public boolean overlaps(DateRange range){
        return this.includes(range.getStart()) || this.includes(range.getEnd()) || range.includes(start);
    }

    @Override
    public int compareTo(DateRange range) {
        if(start.equals(range.getStart())){
            return end.compareTo(range.getEnd());
        }
        return start.compareTo(range.getStart());
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DateRange){
            DateRange otherRange = (DateRange) obj;
            return start.equals(otherRange.getStart()) && end.equals(otherRange.getEnd());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
