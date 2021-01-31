package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, PostId> {
//    Optional<Post> findByPId(PostId postId);

    List<Post> findByAuthor(UserId userId);
    //TODO check on which methods/queries are included by default
}
