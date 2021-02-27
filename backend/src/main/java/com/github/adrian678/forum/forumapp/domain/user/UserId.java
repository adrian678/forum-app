package com.github.adrian678.forum.forumapp.domain.user;

import java.io.Serializable;
import java.util.UUID;

public class UserId implements Serializable {
    private final UUID uuid;

    public UserId(UUID uuid){
        this.uuid = uuid;
    }

    public static UserId fromString(String s){
        return new UserId(UUID.fromString(s));
    }

    public static UserId randomId(){
        return new UserId(UUID.randomUUID());
    }

    public UserId copy(){
        return new UserId(uuid);
    }

    public static UserId getInstance(){
        return new UserId(UUID.randomUUID());
    }

    public UUID getUuid() {
        return uuid;
    }

    public String toString(){
        return uuid.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserId){
            return uuid.equals(((UserId) obj).uuid);
        }
        return false;
    }

    //TODO make a hash method
}
