package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.user.UserId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoardCreationDto implements Serializable {
        String topic;
        String description;
        String owner;   //TODO UserId is serializable, but test whether it is sent properly
        List<String> rules;

    //TODO create either factory method or constructor
    public BoardCreationDto(String topic, String description, String owner, List<String> rules){
        this.topic = topic;
        this.description = description;
        this.owner = owner;
        this.rules = new ArrayList<>(rules);
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }
}
