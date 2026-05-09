package com.example.photoalbum.dto;

import java.sql.Timestamp;
import java.util.List;

public class PhotoDto {
    private Long id;
    private String filePath;
    private String thumbnailPath;
    private String authorUsername;
    private Long authorId;
    private int likes;
    private int dislikes;
    private Integer currentUserVote;
    private List<String> tags;
    private Timestamp createdAt;

    public PhotoDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getThumbnailPath() { return thumbnailPath; }
    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }
    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
    public int getDislikes() { return dislikes; }
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }
    public Integer getCurrentUserVote() { return currentUserVote; }
    public void setCurrentUserVote(Integer currentUserVote) { this.currentUserVote = currentUserVote; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}