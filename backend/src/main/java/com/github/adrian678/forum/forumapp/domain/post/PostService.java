package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.board.Board;
import com.github.adrian678.forum.forumapp.domain.board.BoardNotFoundException;
import com.github.adrian678.forum.forumapp.domain.board.BoardRepository;
import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    BoardRepository boardRepository;

    //TODO test this
//    public void archivePost(String userId, String postId){
//        Post post = postRepository.findById(PostId.fromString(postId)).orElseThrow(() -> new PostNotFoundException("no such Post exists"));
//        Board board = boardRepository.findById(post.getBoardName()).orElseThrow(() -> new BoardNotFoundException("No matching board found"));
//        if(board.getModerators().contains(UserId.fromString(userId))){ //TODO check is the user is the owner
//            post.archive();
//        }
//        //TODO should throw an unauthorized exception is person is not a moderator
//    }
//
//    public void pinPost(String userId, String postId){
//        PostId pId = PostId.fromString(postId);
//        Post post = postRepository.findById(pId).orElseThrow(() -> new PostNotFoundException("no such Post exists"));
//        Board board = boardRepository.findById(post.getBoardName()).orElseThrow(()-> new BoardNotFoundException("No matching Board found"));
//        if(board.getModerators().contains(UserId.fromString(userId))){  //TODO check is the user is the owner
//           board.addPinnedPost(pId);
//        }
//    }
}
