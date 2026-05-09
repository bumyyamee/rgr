package com.example.photoalbum.controller;

import com.example.photoalbum.dto.UserDto;
import com.example.photoalbum.model.Report;
import com.example.photoalbum.repository.ReportRepository;
import com.example.photoalbum.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
public class AdminController {
    private final UserService userService;
    private final ReportRepository reportRepository;

    public AdminController(UserService userService, ReportRepository reportRepository) {
        this.userService = userService;
        this.reportRepository = reportRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> changeRole(@PathVariable Long id, @RequestBody String role) {
        userService.updateRole(id, role);
        return ResponseEntity.ok("Role updated");
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getReports() {
        return ResponseEntity.ok(reportRepository.findAll());
    }

    @PutMapping("/reports/{id}")
    public ResponseEntity<?> handleReport(@PathVariable Long id, @RequestParam String action) {
        // action = "APPROVE" или "REJECT"
        reportRepository.updateStatus(id, action);
        return ResponseEntity.ok("Report handled");
    }
}