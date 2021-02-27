package com.github.adrian678.forum.forumapp.domain.post;


public class PostCreationRequestDto {
    private String author;
    private String boardName;
    private String title;
    private String content;
    //TODO add booleans userHasLiked and userHasSaved
    boolean isLikedByUser;

    boolean isSavedByUser;

    public PostCreationRequestDto(String author, String boardName, String title, String content){
        this.author = author;
        this.boardName = boardName;
        this.title = title;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
    }

    public boolean isSavedByUser() {
        return isSavedByUser;
    }

    public void setSavedByUser(boolean savedByUser) {
        isSavedByUser = savedByUser;
    }
}
