package com.github.adrian678.forum.forumapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EventController implements ApplicationListener<DomainEvent> {

    @Autowired
    EventRepository eventRepository;

    @Override
    public void onApplicationEvent(DomainEvent event) {
        eventRepository.save(event);
    }
}
