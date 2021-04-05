package com.github.adrian678.forum.forumapp.domain;


import java.io.Serializable;
import java.util.UUID;

public class EventId implements Serializable {
    private final UUID uuid;

    private EventId(UUID uuid){
        this.uuid = uuid;
    }

    public static EventId randomId(){
        return new EventId(UUID.randomUUID());
    }
    public static EventId fromString(String s){
        return new EventId(UUID.fromString(s));
    }

    public EventId copy(){
        return new EventId(uuid);
    }
    public UUID getUuid(){
        return uuid;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EventId){
            return uuid.equals(((EventId) obj).uuid);
        }
        return false;
    }
}

