package com.github.adrian678.forum.forumapp.domain.board;

import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardTest {
    String validBoardName = "boardName";
    String validBoardDescription = "description";
    String validUsername = "username";
    String validPassword = "password";
    String validEmail = "test@test.com";
    List<String> validBoardRules = new ArrayList<String>();

    @Test
    @DisplayName("Given a valid subscribed user and board" +
            "When changeOwner is called with the subscribed user" +
            "Then true will be returned")
    public void changeOwnerValidUserTest(){
        User oldOwner = User.createNewUser("oldOwner", validPassword, validEmail);
        User newOwner = User.createNewUser("newOwner", validPassword, validEmail);
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, oldOwner.getUsername(), validBoardRules);
        newOwner.addSubscription(board);
        assert(board.changeOwner(newOwner));
        assert(board.getOwner().equals(newOwner.getUsername()));
    }

    @Test
    @DisplayName("Given a valid subscribed user and board" +
            "When changeOwner is called with the subscribed user" +
            "Then true will be returned")
    public void changeOwnerInvalidUserTest(){
        User oldOwner = User.createNewUser("oldOwner", validPassword, validEmail);
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, oldOwner.getUsername(), validBoardRules);
        assertThrows(IllegalArgumentException.class, ()->board.changeOwner(null));
    }

    @Test
    @DisplayName("Given a valid subscribed user and board" +
            "When changeOwner is called with the subscribed user" +
            "Then true will be returned")
    public void changeOwnerRemovedBoardTest(){
        User oldOwner = User.createNewUser("oldOwner", validPassword, validEmail);
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, oldOwner.getUsername(), validBoardRules);
        board.setRemoved(true);
        assertThrows(BoardRemovedException.class, ()->board.changeOwner(null));
    }

    @Test
    @DisplayName("Given a valid moderator" +
            "When hasModerator is called" +
            "Then true will be returned")
    public void hasModeratorValidModeratorTest(){
        User owner = User.createNewUser(validUsername, validPassword, validEmail);
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, owner.getUsername(), validBoardRules);
    }

    @Test
    @DisplayName("Given a valid subscribed user" +
            "When addModerator is called" +
            "Then true user will be added as a moderator")
    public void addModeratorValidUserTest(){
        User owner = User.createNewUser("owner", validPassword, validEmail);
        User subscribedUser = User.createNewUser(validUsername, validPassword, validEmail);
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, owner.getUsername(), validBoardRules);
        subscribedUser.addSubscription(board);
        assert(board.addModerator(subscribedUser));
    }

    @Test
    @DisplayName("Given a valid subscribed user" +
            "When addModerator is called" +
            "Then true user will be added as a moderator")
    public void addModeratorInValidUserTest(){
        User owner = User.createNewUser(validUsername, validPassword, validEmail);
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, owner.getUsername(), validBoardRules);
        assertThrows(IllegalArgumentException.class, ()-> board.addModerator(null));
    }

    @Test
    @DisplayName("Given a valid subscribed user and a removed board" +
            "When addModerator is called" +
            "Then an exception is thrown")
    public void addModeratorRemovedBoardTest(){
        User owner = User.createNewUser(validUsername, validPassword, validEmail);
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, owner.getUsername(), validBoardRules);
        board.setRemoved(true);
        assertThrows(BoardRemovedException.class, ()-> board.addModerator(null));
    }
    @Test
    @DisplayName("Given a valid post and board" +
            "When addPinnedPost is called" +
            "Then true is returned and the post is added")
    public void addPinnedPostVallidPost(){
        User poster = User.createNewUser(validUsername, validPassword, validEmail);
        Post post = Post.createPost(poster.getUsername(), validBoardName, "title", "content");
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, poster.getUsername(), validBoardRules);
        assert(board.addPinnedPost(post));
        assert(board.getPinnedPosts().contains(post.getpId()));
    }
}
