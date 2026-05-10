package com.example.photoalbum.model;

import java.sql.Timestamp;

public class Photo {
    private Long id;
    private Long userId;
    private String filePath;
    private String thumbnailPath;
    private String privacy;   // !!
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Photo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getThumbnailPath() { return thumbnailPath; }
    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }
    public String getPrivacy() { return privacy; } //!!
    public void setPrivacy(String privacy) { this.privacy = privacy; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}