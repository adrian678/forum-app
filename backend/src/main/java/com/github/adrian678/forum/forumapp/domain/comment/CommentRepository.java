package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.PostId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, CommentId> {
//    List<Comment> findByCId(CommentId commentId); //TODO create a findByParentPost query
    Page<Comment> findByParentPostId(PostId postId, Pageable pageRequest);

}
