package com.example.photoalbum.controller;

import com.example.photoalbum.dto.PhotoDto;
import com.example.photoalbum.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<PhotoDto> upload(@RequestParam("file") MultipartFile file,
                                           @RequestParam("albumId") Long albumId,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        String email = currentUser != null ? currentUser.getUsername() : null;//передаем в сервис что незарег
        return ResponseEntity.ok(photoService.upload(file, albumId, email));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PhotoDto>> feed(@RequestParam(required = false) String privacy,
                                               @RequestParam(required = false) List<String> tags,
                                               @RequestParam(required = false) String dateFrom,
                                               @RequestParam(required = false) String dateTo,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size,
                                               @AuthenticationPrincipal UserDetails currentUser) {
        String email = currentUser != null ? currentUser.getUsername() : null;
        return ResponseEntity.ok(photoService.getFeed(email, privacy, tags, dateFrom, dateTo, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoDto> getById(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails currentUser) {
        String email = currentUser != null ? currentUser.getUsername() : null;
        return ResponseEntity.ok(photoService.getPhotoById(id, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        photoService.deletePhoto(id, currentUser.getUsername());
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/{id}/copy")
    public ResponseEntity<PhotoDto> copy(@PathVariable Long id,
                                         @RequestParam Long targetAlbumId,
                                         @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(photoService.copyPhoto(id, targetAlbumId, currentUser.getUsername()));
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rate(@PathVariable Long id,
                                  @RequestParam int value,
                                  @AuthenticationPrincipal UserDetails currentUser) {
        photoService.ratePhoto(id, currentUser.getUsername(), value);
        return ResponseEntity.ok("Rated");
    }
}