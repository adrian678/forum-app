package com.github.adrian678.forum.forumapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    //TODO return Resources for Spring HATEOAS + proper RESTful interface
    Optional<User> findByUid(long id);


}
