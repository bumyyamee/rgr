/*
package com.example.photoalbum.controller;

import com.example.photoalbum.dto.PhotoDto;
import com.example.photoalbum.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
                                           Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(photoService.upload(file, albumId, username));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PhotoDto>> feed(@RequestParam(required = false) String privacy,
                                               @RequestParam(required = false) List<String> tags,
                                               @RequestParam(required = false) String dateFrom,
                                               @RequestParam(required = false) String dateTo,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size,
                                               Authentication auth) {
        String username = auth.getName(); // или anonymous
        return ResponseEntity.ok(photoService.getFeed(username, privacy, tags, dateFrom, dateTo, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoDto> getById(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(photoService.getPhotoById(id, auth != null ? auth.getName() : null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        photoService.deletePhoto(id, auth.getName());
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/{id}/copy")
    public ResponseEntity<PhotoDto> copy(@PathVariable Long id,
                                         @RequestParam Long targetAlbumId,
                                         Authentication auth) {
        return ResponseEntity.ok(photoService.copyPhoto(id, targetAlbumId, auth.getName()));
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rate(@PathVariable Long id,
                                  @RequestParam int value, // 1 или -1
                                  Authentication auth) {
        photoService.ratePhoto(id, auth.getName(), value);
        return ResponseEntity.ok("Rated");
    }
}*/
