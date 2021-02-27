package com.github.adrian678.forum.forumapp.domain.reaction;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

public interface PostReactionRepository extends MongoRepository<Reaction, PostReactionId> {
}
