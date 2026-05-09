package com.example.photoalbum.repository;

import com.example.photoalbum.model.Tag;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TagRepository {
    private final JdbcTemplate jdbc;

    public TagRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Tag> findByName(String name) {
        try {
            Tag tag = jdbc.queryForObject("SELECT * FROM tags WHERE name = ?",
                    new BeanPropertyRowMapper<>(Tag.class), name);
            return Optional.ofNullable(tag);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Tag save(Tag tag) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("tags")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", tag.getName());
        Long id = insert.executeAndReturnKey(params).longValue();
        tag.setId(id);
        return tag;
    }

    public Tag getOrCreate(String name) {
        return findByName(name).orElseGet(() -> {
            Tag newTag = new Tag();
            newTag.setName(name);
            return save(newTag);
        });
    }

    public List<Tag> findByPhotoId(Long photoId) {
        return jdbc.query(
                "SELECT t.* FROM tags t JOIN photo_tags pt ON t.id = pt.tag_id WHERE pt.photo_id = ?",
                new BeanPropertyRowMapper<>(Tag.class), photoId);
    }

    public void addTagToPhoto(Long photoId, Long tagId) {
        jdbc.update("INSERT INTO photo_tags (photo_id, tag_id) VALUES (?, ?) ON CONFLICT DO NOTHING",
                photoId, tagId);
    }
}