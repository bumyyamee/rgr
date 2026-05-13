package com.example.photoalbum.controller;

import com.example.photoalbum.dto.AlbumDto;
import com.example.photoalbum.dto.PhotoDto;
import com.example.photoalbum.service.AlbumService;
import com.example.photoalbum.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;

    public AlbumController(AlbumService albumService, PhotoService photoService) {
        this.albumService = albumService;
        this.photoService = photoService;
    }

    @PostMapping
    public ResponseEntity<AlbumDto> createAlbum(@RequestParam String title,
                                                @RequestParam(required = false, defaultValue = "") String description,
                                                @RequestParam(required = false, defaultValue = "PRIVATE") String privacy,
                                                @AuthenticationPrincipal UserDetails currentUser) {
        String email = currentUser.getUsername();
        return ResponseEntity.ok(albumService.createAlbum(title, description, privacy, email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDto> getAlbumById(@PathVariable Long id,
                                                 @AuthenticationPrincipal UserDetails currentUser) {
        String email = currentUser != null ? currentUser.getUsername() : null;
        return ResponseEntity.ok(albumService.getAlbumById(id, email));
    }

    @GetMapping("/my")
    public ResponseEntity<List<AlbumDto>> getMyAlbums(@AuthenticationPrincipal UserDetails currentUser) {
        String email = currentUser.getUsername();
        return ResponseEntity.ok(albumService.getUserAlbums(email));
    }
    
    @GetMapping("/{id}/photos")
    public ResponseEntity<List<PhotoDto>> getAlbumPhotos(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails currentUser) {
        String email = currentUser != null ? currentUser.getUsername() : null;
        return ResponseEntity.ok(albumService.getAlbumPhotos(id, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlbum(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails currentUser) {
        albumService.deleteAlbum(id, currentUser.getUsername());
        return ResponseEntity.ok("Album deleted");
    }
}