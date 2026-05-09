/*
package com.example.photoalbum.repository;

import com.example.photoalbum.model.Comment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class CommentRepository {
    private final JdbcTemplate jdbc;

    public CommentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Comment> findByPhotoId(Long photoId) {
        return jdbc.query("SELECT * FROM comments WHERE photo_id = ? ORDER BY created_at ASC",
                new BeanPropertyRowMapper<>(Comment.class), photoId);
    }

    public Comment save(Comment comment) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("comments")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", comment.getUserId());
        params.put("photo_id", comment.getPhotoId());
        params.put("content", comment.getContent());
        params.put("created_at", new Timestamp(System.currentTimeMillis()));
        Long id = insert.executeAndReturnKey(params).longValue();
        comment.setId(id);
        return comment;
    }

    public void delete(Long id) {
        jdbc.update("DELETE FROM comments WHERE id = ?", id);
    }

    public Optional<Comment> findById(Long id) {
        try {
            Comment comment = jdbc.queryForObject("SELECT * FROM comments WHERE id = ?",
                    new BeanPropertyRowMapper<>(Comment.class), id);
            return Optional.ofNullable(comment);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}*/
