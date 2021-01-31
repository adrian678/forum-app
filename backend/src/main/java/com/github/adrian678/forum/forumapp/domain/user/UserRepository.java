package com.github.adrian678.forum.forumapp.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAll();
    //TODO return Resources for Spring HATEOAS + proper RESTful interface
//    Optional<User> findByUid(long id);


}
