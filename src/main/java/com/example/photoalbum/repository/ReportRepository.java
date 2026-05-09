/*
package com.example.photoalbum.repository;

import com.example.photoalbum.model.Report;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class ReportRepository {
    private final JdbcTemplate jdbc;

    public ReportRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Report save(Report report) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("reports")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("reporter_id", report.getReporterId());
        params.put("photo_id", report.getPhotoId());
        params.put("reason", report.getReason());
        params.put("status", "PENDING");
        params.put("created_at", new Timestamp(System.currentTimeMillis()));
        Long id = insert.executeAndReturnKey(params).longValue();
        report.setId(id);
        return report;
    }

    public List<Report> findAll() {
        return jdbc.query("SELECT * FROM reports ORDER BY created_at DESC",
                new BeanPropertyRowMapper<>(Report.class));
    }

    public void updateStatus(Long id, String status) {
        jdbc.update("UPDATE reports SET status = ? WHERE id = ?", status, id);
    }

    public Optional<Report> findById(Long id) {
        try {
            Report report = jdbc.queryForObject("SELECT * FROM reports WHERE id = ?",
                    new BeanPropertyRowMapper<>(Report.class), id);
            return Optional.ofNullable(report);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}*/
