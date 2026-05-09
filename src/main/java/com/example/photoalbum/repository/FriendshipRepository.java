package com.example.photoalbum.repository;

import com.example.photoalbum.model.Friendship;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class FriendshipRepository {
    private final JdbcTemplate jdbc;

    public FriendshipRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Friendship> findByUserAndFriend(Long userId, Long friendId) {
        try {
            Friendship f = jdbc.queryForObject(
                    "SELECT * FROM friendships WHERE user_id = ? AND friend_id = ?",
                    new BeanPropertyRowMapper<>(Friendship.class), userId, friendId);
            return Optional.ofNullable(f);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Friendship save(Friendship friendship) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("friendships")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", friendship.getUserId());
        params.put("friend_id", friendship.getFriendId());
        params.put("status", friendship.getStatus());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        params.put("created_at", now);
        params.put("updated_at", now);
        Long id = insert.executeAndReturnKey(params).longValue();
        friendship.setId(id);
        return friendship;
    }

    public void updateStatus(Long id, String status) {
        jdbc.update("UPDATE friendships SET status = ?, updated_at = ? WHERE id = ?",
                status, new Timestamp(System.currentTimeMillis()), id);
    }

    public List<Friendship> getFriends(Long userId) {
        // accepted friends (both directions)
        return jdbc.query(
                "SELECT * FROM friendships WHERE (user_id = ? OR friend_id = ?) AND status = 'ACCEPTED'",
                new BeanPropertyRowMapper<>(Friendship.class), userId, userId);
    }

    public List<Friendship> getPendingRequests(Long userId) {
        return jdbc.query(
                "SELECT * FROM friendships WHERE friend_id = ? AND status = 'PENDING'",
                new BeanPropertyRowMapper<>(Friendship.class), userId);
    }
}