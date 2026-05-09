package com.example.photoalbum.repository;

import com.example.photoalbum.model.Rating;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class RatingRepository {
    private final JdbcTemplate jdbc;

    public RatingRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Rating> findByUserAndPhoto(Long userId, Long photoId) {
        try {
            Rating rating = jdbc.queryForObject(
                    "SELECT * FROM ratings WHERE user_id = ? AND photo_id = ?",
                    new BeanPropertyRowMapper<>(Rating.class), userId, photoId);
            return Optional.ofNullable(rating);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Rating save(Rating rating) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("ratings")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", rating.getUserId());
        params.put("photo_id", rating.getPhotoId());
        params.put("value", rating.getValue());
        params.put("created_at", new Timestamp(System.currentTimeMillis()));
        Long id = insert.executeAndReturnKey(params).longValue();
        rating.setId(id);
        return rating;
    }

    public void update(Rating rating) {
        jdbc.update("UPDATE ratings SET value = ? WHERE id = ?", rating.getValue(), rating.getId());
    }

    public void delete(Long id) {
        jdbc.update("DELETE FROM ratings WHERE id = ?", id);
    }

    public int countLikes(Long photoId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM ratings WHERE photo_id = ? AND value = 1", Integer.class, photoId);
        return count != null ? count : 0;
    }

    public int countDislikes(Long photoId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM ratings WHERE photo_id = ? AND value = -1", Integer.class, photoId);
        return count != null ? count : 0;
    }
}