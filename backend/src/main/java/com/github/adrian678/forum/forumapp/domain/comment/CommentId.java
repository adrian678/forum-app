package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.PostId;

import java.io.Serializable;
import java.util.UUID;


public class CommentId implements Serializable {
    private UUID cId;

    public CommentId(){
        this.cId = UUID.randomUUID();
    }

    public CommentId(String id){
        cId = UUID.fromString(id);
    }

    @Override
    public String toString() {
        return cId.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PostId){
            return cId.equals(((CommentId) obj).cId);
        }
        return false;
    }
}
