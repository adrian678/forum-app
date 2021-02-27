package com.github.adrian678.forum.forumapp.domain.board;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepository extends MongoRepository<Board, String> {
}
