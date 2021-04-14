package com.github.adrian678.forum.forumapp.domain.post;

import com.github.adrian678.forum.forumapp.domain.user.UserId;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

public class PostResponseDto extends RepresentationModel<PostResponseDto> {
    private PostId postId;
    private String boardName;
    private String author;
    private String content;
    private String title;
    private int points;
    private Instant createdAt;
    private boolean isLikedByUser = false;
    private boolean isSavedByUser = false;
    //TODO add isRemoved field

    public PostResponseDto(PostId postId, String boardName, String author, String content, String title, int points, Instant createdAt){
        this.postId = postId;
        this.boardName = boardName;
        this.author = author;
        this.content = content;
        this.title = title;
        this.points = points;
        this.createdAt = createdAt;
    }

    public static PostResponseDto fromPost(Post post){
        return new PostResponseDto(
                post.getpId(), post.getBoardName(), post.getAuthor(),
                post.getContent(), post.getTitle(), post.getPoints(), post.createdAt()
        );
    }

    public void setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
    }

    public void setSavedByUser(boolean savedByUser) {
        isSavedByUser = savedByUser;
    }

    public PostId getPostId() {
        return postId;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public int getPoints() {
        return points;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public boolean isSavedByUser() {
        return isSavedByUser;
    }
}
