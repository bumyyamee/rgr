package com.example.photoalbum.service;

import com.example.photoalbum.dto.AlbumDto;
import com.example.photoalbum.dto.PhotoDto;
import com.example.photoalbum.exception.ResourceNotFoundException;
import com.example.photoalbum.model.Album;
import com.example.photoalbum.model.Photo;
import com.example.photoalbum.model.User;
import com.example.photoalbum.repository.AlbumRepository;
import com.example.photoalbum.repository.PhotoRepository;
import com.example.photoalbum.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    public AlbumService(AlbumRepository albumRepository,
                        PhotoRepository photoRepository,
                        UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    public AlbumDto createAlbum(String title, String description, String privacy, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Album album = new Album();
        album.setTitle(title);
        album.setDescription(description);
        album.setPrivacy(privacy);
        album.setUserId(user.getId());
        Long id = albumRepository.save(album);
        album.setId(id);
        return convertToDto(album);
    }

    public List<AlbumDto> getUserAlbums(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        return albumRepository.findByUserId(user.getId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AlbumDto getAlbumById(Long albumId, String email) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Альбом не найден"));

        User currentUser = (email != null) ? userRepository.findByEmail(email).orElse(null) : null;

        if (!canViewAlbum(album, currentUser)) {
            throw new RuntimeException("Access denied");
        }
        return convertToDto(album);
    }

    public List<PhotoDto> getAlbumPhotos(Long albumId, String email) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Альбом не найден"));
        User currentUser = (email != null) ? userRepository.findByEmail(email).orElse(null) : null;

        if (!canViewAlbum(album, currentUser)) {
            throw new RuntimeException("Access denied");
        }

        List<Photo> photos = photoRepository.findByAlbumId(albumId);
        return photos.stream()
                .map(p -> {
                    PhotoDto dto = new PhotoDto();
                    dto.setId(p.getId());
                    dto.setFilePath("/uploads/" + p.getFilePath());
                    dto.setPrivacy(p.getPrivacy());
                    dto.setCreatedAt(p.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void addPhotoToAlbum(Long albumId, Long photoId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Альбом не найден"));
        if (!album.getUserId().equals(user.getId())) {
            throw new RuntimeException("Не ваш альбом");
        }
        albumRepository.addPhoto(albumId, photoId);
    }

    @Transactional
    public void deleteAlbum(Long albumId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Альбом не найден"));
        if (!album.getUserId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Нет прав на удаление");
        }
        albumRepository.delete(albumId);
    }

    private boolean canViewAlbum(Album album, User currentUser) {
        if ("PUBLIC".equalsIgnoreCase(album.getPrivacy())) return true;
        if (currentUser == null) return false;
        if (album.getUserId().equals(currentUser.getId())) return true;
        if ("ADMIN".equalsIgnoreCase(currentUser.getRole()) || "MODERATOR".equalsIgnoreCase(currentUser.getRole())) return true;
        if ("FRIENDS".equalsIgnoreCase(album.getPrivacy())) {
            return true; //тут должна быть проверка дружбы
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
        dto.setOwnerUsername(albumRepository.getOwnerUsername(album.getUserId()));
        dto.setPhotoCount(albumRepository.getPhotoCount(album.getId()));
        dto.setCreatedAt(album.getCreatedAt());
        return dto;
    }
}