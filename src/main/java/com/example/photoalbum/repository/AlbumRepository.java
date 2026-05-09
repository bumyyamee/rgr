package com.example.photoalbum.repository;

import com.example.photoalbum.model.Album;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class AlbumRepository {
    private final JdbcTemplate jdbc;

    public AlbumRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Album> findById(Long id) {
        try {
            Album album = jdbc.queryForObject("SELECT * FROM albums WHERE id = ?",
                    new BeanPropertyRowMapper<>(Album.class), id);
            return Optional.ofNullable(album);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Album> findByUserId(Long userId) {
        return jdbc.query("SELECT * FROM albums WHERE user_id = ? ORDER BY created_at DESC",
                new BeanPropertyRowMapper<>(Album.class), userId);
    }

    public Long save(Album album) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("albums")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", album.getUserId());
        params.put("title", album.getTitle());
        params.put("description", album.getDescription());
        params.put("privacy", album.getPrivacy());
        params.put("cover_photo_id", album.getCoverPhotoId());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        params.put("created_at", now);
        params.put("updated_at", now);
        return insert.executeAndReturnKey(params).longValue();
    }

    public void update(Album album) {
        jdbc.update("UPDATE albums SET title = ?, description = ?, privacy = ?, cover_photo_id = ?, updated_at = ? WHERE id = ?",
                album.getTitle(), album.getDescription(), album.getPrivacy(),
                album.getCoverPhotoId(), new Timestamp(System.currentTimeMillis()), album.getId());
    }

    public void delete(Long id) {
        jdbc.update("DELETE FROM albums WHERE id = ?", id);
    }

    public void addPhoto(Long albumId, Long photoId) {
        jdbc.update("INSERT INTO album_photos (album_id, photo_id) VALUES (?, ?) ON CONFLICT DO NOTHING",
                albumId, photoId);
    }

    public void removePhoto(Long albumId, Long photoId) {
        jdbc.update("DELETE FROM album_photos WHERE album_id = ? AND photo_id = ?", albumId, photoId);
    }

    public int getPhotoCount(Long albumId) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM album_photos WHERE album_id = ?",
                Integer.class, albumId);
        return count != null ? count : 0;
    }

    public String getOwnerUsername(Long userId) {
        return jdbc.queryForObject("SELECT username FROM users WHERE id = ?", String.class, userId);
    }

    public Optional<Album> findAlbumByPhotoId(Long photoId) {
        try {
            Album album = jdbc.queryForObject(
                    "SELECT a.* FROM albums a JOIN album_photos ap ON a.id = ap.album_id WHERE ap.photo_id = ?",
                    new BeanPropertyRowMapper<>(Album.class), photoId);
            return Optional.ofNullable(album);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}