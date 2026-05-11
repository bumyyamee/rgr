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

    
    public double getAverageRating(Long photoId) {
        Double avg = jdbc.queryForObject(
                "SELECT COALESCE(AVG(value), 0.0) FROM ratings WHERE photo_id = ?",
                Double.class, photoId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    //общее колво
    public int getRatingCount(Long photoId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM ratings WHERE photo_id = ?", Integer.class, photoId);
        return count != null ? count : 0;
    }

    //текущ
    public Integer findUserRating(Long photoId, Long userId) {
        try {
            Integer value = jdbc.queryForObject(
                    "SELECT value FROM ratings WHERE photo_id = ? AND user_id = ?",
                    Integer.class, photoId, userId);
            return value;
        } catch (Exception e) {
            return null;
        }
    }
}