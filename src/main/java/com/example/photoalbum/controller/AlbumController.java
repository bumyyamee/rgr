package com.example.photoalbum.controller;

import com.example.photoalbum.dto.AlbumDto;
import com.example.photoalbum.service.AlbumService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) { this.albumService = albumService; }

    @GetMapping
    public ResponseEntity<List<AlbumDto>> getMyAlbums(Authentication auth) {
        Long userId = getCurrentUserId(auth);
        return ResponseEntity.ok(albumService.getUserAlbums(userId));
    }

    @PostMapping
    public ResponseEntity<AlbumDto> create(@RequestBody Map<String, String> body, Authentication auth) {
        Long userId = getCurrentUserId(auth);
        AlbumDto dto = albumService.createAlbum(body.get("title"), body.get("description"), body.get("privacy"), userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDto> getById(@PathVariable Long id, Authentication auth) {
        Long currentUserId = getCurrentUserId(auth);
        return ResponseEntity.ok(albumService.getAlbumById(id, currentUserId));
    }

    private Long getCurrentUserId(Authentication auth) {
        // получаем через UserService по имени
        return null; // упрощённо, будет инжектирован UserService и взят ID
    }
}