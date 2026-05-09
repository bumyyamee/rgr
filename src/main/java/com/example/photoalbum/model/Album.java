package com.example.photoalbum.model;

import java.sql.Timestamp;

public class Album {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String privacy;
    private Long coverPhotoId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Album() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPrivacy() { return privacy; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }
    public Long getCoverPhotoId() { return coverPhotoId; }
    public void setCoverPhotoId(Long coverPhotoId) { this.coverPhotoId = coverPhotoId; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}