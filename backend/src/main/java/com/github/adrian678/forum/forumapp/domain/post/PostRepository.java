package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, PostId> {
    List<Post> findByTopic(String topic);

    List<Post> findByAuthor(UserId userId);
    //TODO check on which methods/queries are included by default
}
