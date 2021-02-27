package com.github.adrian678.forum.forumapp.domain.post;

import java.io.Serializable;
import java.util.UUID;


public class PostId implements Serializable {
    private UUID uuid;

    private PostId(UUID uuid){
        this.uuid = uuid;
    }

    public static PostId randomId(){
        return new PostId(UUID.randomUUID());
    }
    public static PostId fromString(String s){
        return new PostId(UUID.fromString(s));
    }

    public UUID getUuid(){
        return uuid;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PostId){
            return uuid.equals(((PostId) obj).uuid);
        }
        return false;
    }
}
