package com.github.adrian678.forum.forumapp.domain;

import com.github.adrian678.forum.forumapp.domain.user.UserCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    @Autowired
    EventRepository eventRepository;

    @org.springframework.context.event.EventListener
    void persistDomainEvent(DomainEvent event){
//        System.out.println("event received by listener");
        eventRepository.save(event);
    }

    //TODO write an event listener for BoardCreatedEvent to save the change to the User?
}
