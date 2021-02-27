package com.github.adrian678.forum.forumapp.domain.report;

import java.util.UUID;

public class ReportId {

    private UUID uuid;

    private ReportId(UUID uuid){
        this.uuid = uuid;
    }

    public static ReportId randomId(){
        return new ReportId(UUID.randomUUID());
    }

    public static ReportId fromString(String s){
        return new ReportId(UUID.fromString(s));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ReportId){
            return uuid.equals(((ReportId) obj).uuid);
        }
        return false;
    }

    public UUID getUuid(){
        return uuid;
    }
}
