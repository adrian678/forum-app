package com.github.adrian678.forum.forumapp.domain.board;

import java.io.Serializable;
import java.util.UUID;

public class BoardId implements Serializable {
    private UUID bId;

    public BoardId(){
        this.bId = UUID.randomUUID();
    }

    public BoardId(String bId){
        this.bId = UUID.fromString(bId);
    }

    private UUID getId(){ //TODO rename to 'toUUID'?
        return bId;
    }

    public String toString(){
        return bId.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BoardId){
            return bId.equals(((BoardId) obj).getId());
        }
        return false;
    }
}
