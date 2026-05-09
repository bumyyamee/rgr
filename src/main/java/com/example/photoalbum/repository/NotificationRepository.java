/*
package com.example.photoalbum.repository;

import com.example.photoalbum.model.Notification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class NotificationRepository {
    private final JdbcTemplate jdbc;

    public NotificationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Notification save(Notification notification) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("notifications")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", notification.getUserId());
        params.put("message", notification.getMessage());
        params.put("is_read", notification.isRead());
        params.put("created_at", new Timestamp(System.currentTimeMillis()));
        Long id = insert.executeAndReturnKey(params).longValue();
        notification.setId(id);
        return notification;
    }

    public List<Notification> findByUserId(Long userId) {
        return jdbc.query("SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC",
                new BeanPropertyRowMapper<>(Notification.class), userId);
    }
}*/
