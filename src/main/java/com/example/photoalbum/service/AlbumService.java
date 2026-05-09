/*
package com.example.photoalbum.service;

import com.example.photoalbum.dto.AlbumDto;
import com.example.photoalbum.exception.ResourceNotFoundException;
import com.example.photoalbum.model.Album;
import com.example.photoalbum.model.Photo;
import com.example.photoalbum.repository.AlbumRepository;
import com.example.photoalbum.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    public AlbumService(AlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
    }

    public AlbumDto createAlbum(String title, String description, String privacy, Long userId) {
        Album album = new Album();
        album.setTitle(title);
        album.setDescription(description);
        album.setPrivacy(privacy);
        album.setUserId(userId);
        Long id = albumRepository.save(album);
        album.setId(id);
        return convertToDto(album);
    }

    public List<AlbumDto> getUserAlbums(Long userId) {
        return albumRepository.findByUserId(userId).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public AlbumDto getAlbumById(Long albumId, Long currentUserId) {
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new ResourceNotFoundException("Album not found"));
        // проверка доступа на чтение
        if (!canViewAlbum(album, currentUserId)) throw new RuntimeException("Access denied");
        return convertToDto(album);
    }

    @Transactional
    public void addPhotoToAlbum(Long albumId, Long photoId, Long userId) {
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new ResourceNotFoundException("Album not found"));
        if (!album.getUserId().equals(userId)) throw new RuntimeException("Not your album");
        albumRepository.addPhoto(albumId, photoId);
    }

    private boolean canViewAlbum(Album album, Long currentUserId) {
        if (album.getPrivacy().equals("PUBLIC")) return true;
        if (album.getUserId().equals(currentUserId)) return true;
        if (album.getPrivacy().equals("FRIENDS")) {
            return true; // упрощённо, в реальности проверка через FriendshipRepository
        }
        return false;
    }

    private AlbumDto convertToDto(Album album) {
        AlbumDto dto = new AlbumDto();
        dto.setId(album.getId());
        dto.setTitle(album.getTitle());
        dto.setDescription(album.getDescription());
        dto.setPrivacy(album.getPrivacy());
        dto.setCoverPhotoId(album.getCoverPhotoId());
        dto.setOwnerUsername(albumRepository.getOwnerUsername(album.getUserId())); // нужно реализовать
        dto.setPhotoCount(albumRepository.getPhotoCount(album.getId()));
        dto.setCreatedAt(album.getCreatedAt());
        return dto;
    }
}*/
