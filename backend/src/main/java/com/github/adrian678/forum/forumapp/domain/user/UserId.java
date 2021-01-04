package com.github.adrian678.forum.forumapp.domain.user;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class UserId implements Serializable {
    private final UUID uId;

    public UserId(){
        this.uId = UUID.randomUUID();
    }

    private UserId(UUID uuid){
        this.uId = uuid;
    }

    public UserId(UserId userId){
        this.uId = UUID.fromString(userId.stringify());
    }

    public static UserId of(String s){
        return new UserId(UUID.fromString(s));
    }

    public String stringify(){
        return uId.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserId){
            return uId.equals(((UserId) obj).uId);
        }
        return false;
    }
}
