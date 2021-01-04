package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.lang.NonNull;

import javax.persistence.Id;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {

    @Id
    @NonNull //TODO is adding NonNull after ID redundant?
    public final String topic;

    private final String description;

    private Quantity numSubscribers;

    public final Date creationDate;

    private List<UserId> moderators; //TODO how to represent many to many relationship using ID's?

    private List<String> rules;

    public Board(String topic, String description, int subscribers, Date creationDate, List<UserId> mods, List<String > rules){
        this.topic = topic;
        this.description = description;
        this.numSubscribers = Quantity.of(subscribers);
        this.creationDate = (Date) creationDate.clone(); //TODO is defensive programming necessary here and below?
        this.moderators = Collections.unmodifiableList(mods);
        this.rules = Collections.unmodifiableList(rules);
    }

    public static Board createNewBoard(String topic, String description){
        return new Board(topic, description, 0, new Date(System.currentTimeMillis()), new ArrayList<UserId>(), new ArrayList<String>());
    }

}
