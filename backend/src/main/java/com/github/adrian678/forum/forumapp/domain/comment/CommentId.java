package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.PostId;

import java.io.Serializable;
import java.util.UUID;


public class CommentId implements Serializable {
    private UUID uuid;

    private CommentId(UUID uuid){
        this.uuid = uuid;
    }

    public static CommentId fromString(String s){
        return new CommentId(UUID.fromString(s));
    }

    public static CommentId randomId(){
        return new CommentId(UUID.randomUUID());
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PostId){
            return uuid.equals(((CommentId) obj).uuid);
        }
        return false;
    }
}
