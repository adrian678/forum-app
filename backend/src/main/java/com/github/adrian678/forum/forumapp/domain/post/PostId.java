package com.github.adrian678.forum.forumapp.domain.post;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class PostId implements Serializable {
    private UUID pId;
    public PostId(){
        this.pId = UUID.randomUUID();
    }

    public UUID getId(){
        return pId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PostId){
            return pId.equals(((PostId) obj).pId);
        }
        return false;
    }
}
