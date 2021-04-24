package com.github.adrian678.forum.forumapp.domain.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PostTest {
    String author = "author";
    String boardName = "boardName";
    String title = "title'";
    String content = "content";


    @Test
    @DisplayName("Given invalid author as input," +
            "when createPot is called" +
            "then an IllegalArgumentException is thrown")
    public void createPostInvalidAuthordTest(){
        assertThrows(IllegalArgumentException.class, ()-> Post.createPost(null, boardName, title, content));
    }

    @Test
    @DisplayName("Given an invalid board as input," +
            "when createPot is called" +
            "then an IllegalArgumentException is thrown")
    public void createPostInvalidBoardTest(){
        assertThrows(IllegalArgumentException.class, ()-> Post.createPost(author, null, title, content));
    }

    @Test
    @DisplayName("Given invalid title as input," +
            "when createPot is called" +
            "then an IllegalArgumentException is thrown")
    public void createPostInValidTitleTest(){
        String whitespace = "    ";
        assertThrows(IllegalArgumentException.class, ()-> Post.createPost(author, boardName, whitespace, content));
        assertThrows(IllegalArgumentException.class, ()-> Post.createPost(author, boardName, null, content));

    }

    @Test
    @DisplayName("Given a post," +
            "when the post is archived" +
            "then the archived field will be set to true")
    public void archiveTest(){
        Post post = Post.createPost(author, boardName, title, content);
        assert(post.isArchived());
        post.archive();
        assert(false == post.isArchived());
    }


}
