package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, PostId> {
//    Optional<Post> findByPId(PostId postId);

    List<Post> findByAuthor(String userId);
    @Query(sort = "{ createdAt : -1 }")
    Page<Post> findByBoardName(String boardName, Pageable pageable);
    Page<Post> findAllOrderByCreatedAt(Pageable pageable);
    //TODO check on which methods/queries are included by default
}
