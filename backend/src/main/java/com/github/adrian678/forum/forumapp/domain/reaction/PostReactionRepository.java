package com.github.adrian678.forum.forumapp.domain.reaction;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostReactionRepository extends MongoRepository<Reaction, PostReactionId> {

//    Optional<Reaction> findAndReplace(Reaction reaction);
}
