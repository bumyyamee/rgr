package com.example.photoalbum.dto;

import java.sql.Timestamp;
import java.util.List;

public class PhotoDto {
    private Long id;
    private String filePath;
    private String thumbnailPath;
    private String authorUsername;
    private Long authorId;
    private double averageRating;   // по идее 0 если нет оценок
    private int ratingCount;        
    private Integer currentUserVote; // для отслеживания текущей оценки
    private List<String> tags;
    private String privacy;
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
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
    public Integer getCurrentUserVote() { return currentUserVote; }
    public void setCurrentUserVote(Integer currentUserVote) { this.currentUserVote = currentUserVote; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getPrivacy() { return privacy; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}