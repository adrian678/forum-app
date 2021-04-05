package com.github.adrian678.forum.forumapp.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<DomainEvent, EventId> {
}
