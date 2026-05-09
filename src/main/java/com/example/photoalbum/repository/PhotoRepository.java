/*
package com.example.photoalbum.repository;

import com.example.photoalbum.model.Photo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class PhotoRepository {
    private final JdbcTemplate jdbc;

    public PhotoRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Optional<Photo> findById(Long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject("SELECT * FROM photos WHERE id=?", new BeanPropertyRowMapper<>(Photo.class), id));
        } catch (Exception e) { return Optional.empty(); }
    }

    public Long save(Photo photo) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc).withTableName("photos").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", photo.getUserId());
        params.put("file_path", photo.getFilePath());
        params.put("thumbnail_path", photo.getThumbnailPath());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        params.put("created_at", now);
        params.put("updated_at", now);
        return insert.executeAndReturnKey(params).longValue();
    }

    public void delete(Long id) {
        jdbc.update("DELETE FROM photos WHERE id=?", id);
    }

    public List<Photo> findByAlbumId(Long albumId) {
        return jdbc.query("SELECT p.* FROM photos p JOIN album_photos ap ON p.id=ap.photo_id WHERE ap.album_id=?",
                new BeanPropertyRowMapper<>(Photo.class), albumId);
    }

    // Для ленты с фильтрацией (упрощённая версия)
    public List<Photo> findPublicFeed(Long userId, Set<Long> tagIds, Timestamp from, Timestamp to, int page, int size) {
        // В реальности здесь сложный динамический SQL, для примера вернём все публичные фото.
        String sql = "SELECT DISTINCT p.* FROM photos p " +
                "JOIN album_photos ap ON p.id = ap.photo_id " +
                "JOIN albums a ON a.id = ap.album_id " +
                "WHERE a.privacy = 'PUBLIC' " +
                "ORDER BY p.created_at DESC LIMIT ? OFFSET ?";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Photo.class), size, page * size);
    }
}*/
