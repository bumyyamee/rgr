package com.example.photoalbum.dto;

import java.sql.Timestamp;

public class AlbumDto {
    private Long id;
    private String title;
    private String description;
    private String privacy;
    private Long coverPhotoId;
    private String coverPhotoUrl;
    private int photoCount;
    private String ownerUsername;
    private Timestamp createdAt;

    public AlbumDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPrivacy() { return privacy; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }
    public Long getCoverPhotoId() { return coverPhotoId; }
    public void setCoverPhotoId(Long coverPhotoId) { this.coverPhotoId = coverPhotoId; }
    public String getCoverPhotoUrl() { return coverPhotoUrl; }
    public void setCoverPhotoUrl(String coverPhotoUrl) { this.coverPhotoUrl = coverPhotoUrl; }
    public int getPhotoCount() { return photoCount; }
    public void setPhotoCount(int photoCount) { this.photoCount = photoCount; }
    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}