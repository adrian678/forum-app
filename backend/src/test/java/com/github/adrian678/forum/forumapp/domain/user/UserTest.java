package com.github.adrian678.forum.forumapp.domain.user;

import com.github.adrian678.forum.forumapp.domain.board.Board;
import com.github.adrian678.forum.forumapp.domain.comment.Comment;
import com.github.adrian678.forum.forumapp.domain.comment.CommentId;
import com.github.adrian678.forum.forumapp.domain.post.Post;
import com.github.adrian678.forum.forumapp.domain.post.PostId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    String validUsername = "username";
    String validPassword = "password";
    String validEmail = "test@test.com";
    String validBoardName = "boardName";
    String validBoardDescription = "boardName";
    String validBoardOwnerName = "owner";
    List<String> validBoardRules = new ArrayList<String>();
    PostId validPostId = PostId.randomId();
    CommentId validCommentId = CommentId.randomId();

    @Test
    @DisplayName("Given invalid username," +
            "when createNewUser is called," +
            "then IllegalArgumentException is thrown")
    public void createNewUserInvalisUsernameTest(){
        String nullUsername = null;
        String password = "password" ;
        String email = "test@test.com";
        Throwable exception = assertThrows(IllegalArgumentException.class, ()-> User.createNewUser(nullUsername, password, email));
        String emptyUsername = "";
        exception = assertThrows(IllegalArgumentException.class, ()-> User.createNewUser(emptyUsername, password, email));
        String whiteSpace = "   ";
        exception = assertThrows(IllegalArgumentException.class, ()-> User.createNewUser(whiteSpace, password, email));
    }

    @Test
    @DisplayName("Given invalid password," +
            "when createNewUser is called," +
            "then IllegalArgumentException is thrown")
    public void createNewUserInvalidPasswordTest(){
        String username = "username";
        String nullPassword = null;
        String email = "test@test.com";
        Throwable exception = assertThrows(IllegalArgumentException.class, ()-> User.createNewUser(username, nullPassword, email));
        String emptyPassword = "";
        exception = assertThrows(IllegalArgumentException.class, ()-> User.createNewUser(username, emptyPassword, email));
        String whiteSpace = "   ";
        exception = assertThrows(IllegalArgumentException.class, ()-> User.createNewUser(username, whiteSpace, email));
    }

    @Test
    @DisplayName("Given invalid PostId," +
            "when saveNewPost is called," +
            "then IllegalArgumentException is thrown")
    public void saveNewPostInvalidPostIdTest(){
        User user = User.createNewUser(validUsername, validPassword,  validEmail);
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, validBoardOwnerName, validBoardRules);
        Throwable exception = assertThrows(IllegalArgumentException.class, ()-> user.saveNewPost(null));
        Post post = Post.createPost(user.getUsername(), validBoardName, "title", "content");
        exception = assertThrows(IllegalArgumentException.class, ()-> user.saveNewPost(null));
    }

    @Test
    @DisplayName("Given valid PostId," +
            "when saveNewPost called," +
            "then postId added to savedPosts list")
    public void saveNewPostValidPostIdInputTest(){
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        assert(user.getSavedPosts().size() == 0);
        Post post = Post.createPost(user.getUsername(), validBoardName, "title", "content");
        user.saveNewPost(post);
        assert (user.getSavedPosts().size() == 1);
        assert(user.getSavedPosts().contains(post.getpId()));
    }

//    @Test
//    @DisplayName("Given invalid PostId, when removeSavedPost is called then nothing")
//    public void givenInvalidPostIdWhenUserRemovesPostThenExceptionIsThrown(){
//        User user = User.createNewUser(validUsername, validPassword, validEmail);
//        Throwable exception = assertThrows(IllegalArgumentException.class, ()-> user.removeSavedPost(null));
//    }

    @Test
    @DisplayName("Given a valid PostId," +
            "when removeSavedPost is called," +
            "then PostId is removed from savedPosts list")
    public void removeSavedPostValidPostIdTest(){
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        Post post = Post.createPost(user.getUsername(), validBoardName, "title", "content");
        user.saveNewPost(post);
        assert (user.getSavedPosts().size() == 1);
        user.removeSavedPost(post);
        assert (user.getSavedPosts().size() == 0);
    }

    @Test
    @DisplayName("Given valid commentId," +
            "when saveComment called," +
            "then commentId is added to savedComments list")
    public void saveNewCommentValidCommentIdTest(){
        //TODO update by having business logic use a Comment object instead of CommentId
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        Post post = Post.createPost(user.getUsername(), validBoardName, "title", "content");
        Comment comment = Comment.create(post, null, user, "content");
        assert(user.getSavedComments().size() == 0);
        user.saveNewComment(comment);
        assert (user.getSavedComments().size() == 1);
        assert(user.getSavedComments().contains(comment.getCid()));
    }

    //do not need to test removeComment since it thinly wraps the removeComment method of ArrayList

    @Test
    @DisplayName("Given a valid CommentId already in savedComments list," +
            "when removeSavedComment is called" +
            "then CommentId is removed from savedComments list")
    public void removeSavedCommentValidCommentIdMemberTest(){
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        Post post = Post.createPost(user.getUsername(), validBoardName, "title", "content");
        Comment comment = Comment.create(post, null, user, "content");
        user.saveNewComment(comment);
        assert (user.getSavedComments().size() == 1);
        user.removeSavedComment(comment);
        assert (user.getSavedComments().size() == 0);
    }

    @Test
    @DisplayName("Given a valid user to block," +
            "when userToBlock is called," +
            "then the user is added to list of blocked users")
    public void blockUserValidUserTest(){
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        User userToBlock = User.createNewUser("userToBlock", validPassword, validEmail);
        user.blockUser(userToBlock);
        assert(user.hasBlockedUser(userToBlock));
    }

    @Test
    @DisplayName("Given a user to block," +
            "when a user tries to block him or herself," +
            "then an IllegalArgumentException is thrown")
    public void blockUserSameUserTest(){
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> user.blockUser(user));
    }

    @Test
    @DisplayName("Given a valid user," +
            "when followUser is called" +
            "then the valid user is added to the list of followed users")
    public void followUserValidUserTest(){
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        User userToFollow = User.createNewUser("userToFollow", validPassword, validEmail);
        user.followUser(userToFollow);
        assert(user.getFollowedUsers().contains(userToFollow.getUsername()));
    }

    @Test
    @DisplayName("Given an invalid user," +
            "when followUser is called" +
            "then an IllegalArgumentException is thrown")
    public void followUserInValidUserTest(){
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> user.followUser(null));

    }

    @Test
    @DisplayName("Given an valid user," +
            "when the user tries to follow him/herself" +
            "then an IllegalArgumentException is thrown")
    public void followUserSameUserTest(){
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> user.followUser(user));
    }

    @Test
    @DisplayName("Given a boardname," +
            "when a user subscribed to the board" +
            "then the board is added to the users list of subscriptions")
//    TODO change the parameter from using a String to using a Board object
    public void addSubscriptionValidBoardNameTest(){
        User user = User.createNewUser(validUsername, validPassword, validEmail);
        User subScribedUser = User.createNewUser("subScribedUser", validPassword, validEmail);
        Board board = Board.createNewBoard(validBoardName, validBoardDescription, "owner", validBoardRules);
        subScribedUser.addSubscription(board);
        assert(subScribedUser.hasSubscribedTo(board));

    }

//    TODO do tests for:
    @Test
    @DisplayName("Given a valid boardName that the user is banned from" +
            "When isBannedFromBoard is called" +
            "then true is returned")
    public void isBannedFromBoardValidBoardNameTest(){
        User owner = User.createNewUser(validUsername, validPassword, validEmail);
        User bannedUser = User.createNewUser("bannedUser", validPassword, validEmail);
        String boardName = "boardName";
        String boardDescription = "description";
        List<String> boardRules = new ArrayList<String>();
        boardRules.add("one rule");
        Board board = Board.createNewBoard(boardName, boardDescription, owner.getUsername(), boardRules);
        Ban ban = Ban.createPermanent(owner.getUsername(), board);
        bannedUser.addBan(ban);
        System.out.print(ban);      //ban is lazy loaded/garbage collected without this line?
        assert(bannedUser.isBannedFromBoard(board));
    }

    @Test
    @DisplayName("Given a valid boardName that the user is not banned from" +
            "When isBannedFromBoard is called" +
            "then false is returned")
    public void isNotBannedFromBoardValidBoardNameTest(){
        User owner = User.createNewUser(validUsername, validPassword, validEmail);
        User bannedUser = User.createNewUser("bannedUser", validPassword, validEmail);
        String boardName = "boardName";
        String boardDescription = "description";
        List<String> boardRules = new ArrayList<String>();
        boardRules.add("one rule");
        Board board = Board.createNewBoard(boardName, boardDescription, owner.getUsername(), boardRules);
        Ban ban = Ban.createPermanent(owner.getUsername(), board);
//        bannedUser.addBan(ban);
        System.out.print(ban);      //ban is lazy loaded/garbage collected without this line?
        assert(!bannedUser.isBannedFromBoard(board));
    }
//isBannedFromBoard(String boardName)
    //addBan
    //removeBan
    //getBans
    //addSubscription
    //hasSubscribedTo
    //hasFollowedUser
    //hasBLockedUser
    //

}
