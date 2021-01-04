package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.PostId;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class CommentId implements Serializable {
    private UUID cId;

    public CommentId(){
        this.cId = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PostId){
            return cId.equals(((CommentId) obj).cId);
        }
        return false;
    }
}
