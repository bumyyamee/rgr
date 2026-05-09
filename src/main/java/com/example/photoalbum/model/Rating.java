package com.example.photoalbum.model;

import java.sql.Timestamp;

public class Rating {
    private Long id;
    private Long userId;
    private Long photoId;
    private int value; // -1 или 1
    private Timestamp createdAt;

    public Rating() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getPhotoId() { return photoId; }
    public void setPhotoId(Long photoId) { this.photoId = photoId; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}