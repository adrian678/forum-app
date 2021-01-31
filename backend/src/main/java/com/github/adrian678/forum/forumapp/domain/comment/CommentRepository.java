package com.github.adrian678.forum.forumapp.domain.comment;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, CommentId> {
//    List<Comment> findByCId(CommentId commentId); //TODO create a findByParentPost query

}
