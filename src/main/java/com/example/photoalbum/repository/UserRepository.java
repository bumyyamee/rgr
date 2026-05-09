package com.example.photoalbum.repository;

import com.example.photoalbum.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Optional<User> findById(Long id) {
        try {
            User u = jdbc.queryForObject("SELECT * FROM users WHERE id=?", new BeanPropertyRowMapper<>(User.class), id);
            return Optional.ofNullable(u);
        } catch (Exception e) { return Optional.empty(); }
    }

    public Optional<User> findByUsername(String username) {
        try {
            User u = jdbc.queryForObject("SELECT * FROM users WHERE username=?", new BeanPropertyRowMapper<>(User.class), username);
            return Optional.ofNullable(u);
        } catch (Exception e) { return Optional.empty(); }
    }

    public Optional<User> findByEmail(String email) {
        try {
            User u = jdbc.queryForObject("SELECT * FROM users WHERE email=?", new BeanPropertyRowMapper<>(User.class), email);
            return Optional.ofNullable(u);
        } catch (Exception e) { return Optional.empty(); }
    }

    public Long save(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("users").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("email", user.getEmail());
        params.put("password_hash", user.getPasswordHash());
        params.put("role", user.getRole());
        params.put("avatar_path", user.getAvatarPath());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        params.put("created_at", now);
        params.put("updated_at", now);
        return insert.executeAndReturnKey(params).longValue();
    }

    public void update(User user) {
        jdbc.update("UPDATE users SET username=?, email=?, password_hash=?, role=?, avatar_path=?, updated_at=? WHERE id=?",
                user.getUsername(), user.getEmail(), user.getPasswordHash(), user.getRole(), user.getAvatarPath(),
                new Timestamp(System.currentTimeMillis()), user.getId());
    }

    public List<User> findAll(int page, int size) {
        return jdbc.query("SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?",
                new BeanPropertyRowMapper<>(User.class), size, page * size);
    }
}