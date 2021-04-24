package com.github.adrian678.forum.forumapp.domain.comment;

import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentTest {
    String username = "author";
    String validPassword = "password";
    String validEmail = "test@test.com";
    String boardName = "boardName";
    String title = "title";
    String content = "contnet";
    Post post = Post.createPost(username, boardName, title, content);
    User author = User.createNewUser(username, validPassword, validEmail);


    @Test
    @DisplayName("Given invalid post as input," +
            "when createPot is called" +
            "then an IllegalArgumentException is thrown")
    public void createCommentInvalidPostTest(){
        assertThrows(IllegalArgumentException.class, ()-> Comment.create(null, null, author, content));
    }

    @Test
    @DisplayName("Given invalid author as input," +
            "when createPot is called" +
            "then an IllegalArgumentException is thrown")
    public void createCommentInvalidAuthorTest(){
        assertThrows(IllegalArgumentException.class, ()-> Comment.create(post, null, null, content));
    }

}
