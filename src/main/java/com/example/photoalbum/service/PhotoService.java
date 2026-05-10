package com.example.photoalbum.service;

import com.example.photoalbum.dto.PhotoDto;
import com.example.photoalbum.exception.ResourceNotFoundException;
import com.example.photoalbum.model.Album;
import com.example.photoalbum.model.Photo;
import com.example.photoalbum.model.Rating;
import com.example.photoalbum.model.Tag;
import com.example.photoalbum.model.User;
import com.example.photoalbum.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;
    private final TagRepository tagRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    private final FriendService friendService; //использую в проверке на друзей

    public PhotoService(PhotoRepository photoRepository,
                        AlbumRepository albumRepository,
                        TagRepository tagRepository,
                        RatingRepository ratingRepository,
                        UserRepository userRepository,
                        FileStorageService fileStorageService,
                        EmailService emailService,
                        FriendService friendService) {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
        this.tagRepository = tagRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.emailService = emailService;
        this.friendService = friendService;
    }

    @Transactional
    public PhotoDto upload(MultipartFile file, Long albumId, String email) {
        User user = userRepository.findByEmail(email) //логин-почта
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Альбом не найден"));

        if (!album.getUserId().equals(user.getId())) {
            throw new RuntimeException("Вы не можете добавлять фото в чужой альбом");
        }

        String storedFilename = fileStorageService.store(file);

        Photo photo = new Photo();
        photo.setUserId(user.getId());
        photo.setFilePath(storedFilename);
        photo.setPrivacy(album.getPrivacy()); // наследуем приватность альбома
        Long photoId = photoRepository.save(photo);
        photo.setId(photoId);

        albumRepository.addPhoto(albumId, photoId);

        return convertToDto(photo);
    }

    public List<PhotoDto> getFeed(String email, String privacy, List<String> tags,
                                  String dateFrom, String dateTo, int page, int size) {
        User currentUser = (email != null) ? userRepository.findByEmail(email).orElse(null) : null;

        List<Photo> photos = photoRepository.findAllForFeed(page, size); // теперь без джоина с альбомом
        return photos.stream()
                .filter(p -> canAccessPhoto(p, currentUser))  
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PhotoDto getPhotoById(Long id, String email) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фото не найдено"));

        User currentUser = (email != null) ? userRepository.findByEmail(email).orElse(null) : null; //
                                                                                                    //незарег-только паблики
        if (!canAccessPhoto(photo, currentUser)) {                                                  //зарег-по доступу
            throw new RuntimeException("Access denied");                                            //
        }
        return convertToDto(photo);
    }

    @Transactional
    public void deletePhoto(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фото не найдено"));

        if (!photo.getUserId().equals(user.getId())
                && !user.getRole().equals("ADMIN")
                && !user.getRole().equals("MODERATOR")) {
            throw new RuntimeException("У вас нет прав на удаление этого фото");
        }

        fileStorageService.delete(photo.getFilePath());
        photoRepository.delete(id);
    }

    @Transactional
    public PhotoDto copyPhoto(Long photoId, Long targetAlbumId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Photo original = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Исходное фото не найдено"));

        Album targetAlbum = albumRepository.findById(targetAlbumId)
                .orElseThrow(() -> new ResourceNotFoundException("Целевой альбом не найден"));

        if (!targetAlbum.getUserId().equals(user.getId())) {
            throw new RuntimeException("Вы не можете копировать в чужой альбом");
        }

        String newFilename = fileStorageService.copy(original.getFilePath());

        Photo newPhoto = new Photo();
        newPhoto.setUserId(user.getId());
        newPhoto.setFilePath(newFilename);
        newPhoto.setPrivacy(original.getPrivacy()); // сохраняем приватность оригинала
        Long newPhotoId = photoRepository.save(newPhoto);
        newPhoto.setId(newPhotoId);

        albumRepository.addPhoto(targetAlbumId, newPhotoId);

        return convertToDto(newPhoto);
    }

    @Transactional
    public void ratePhoto(Long photoId, String email, int value) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Фото не найдено"));

        Optional<Rating> existingRating = ratingRepository.findByUserAndPhoto(user.getId(), photoId);
        if (existingRating.isPresent()) {
            Rating r = existingRating.get();
            if (r.getValue() == value) {
                ratingRepository.delete(r.getId());
            } else {
                r.setValue(value);
                ratingRepository.update(r);
                if (!photo.getUserId().equals(user.getId())) {
                    User owner = userRepository.findById(photo.getUserId()).orElse(null);
                    if (owner != null) {
                        emailService.sendRatingNotification(owner, photoId.toString(), user.getUsername(), value);
                    }
                }
            }
        } else {
            Rating r = new Rating();
            r.setUserId(user.getId());
            r.setPhotoId(photoId);
            r.setValue(value);
            ratingRepository.save(r);

            if (!photo.getUserId().equals(user.getId())) {
                User owner = userRepository.findById(photo.getUserId()).orElse(null);
                if (owner != null) {
                    emailService.sendRatingNotification(owner, photoId.toString(), user.getUsername(), value);
                }
            }
        }
    }

    private boolean canAccessPhoto(Photo photo, User currentUser) { //проверка по ролям (публ, прив, адм)
        if ("PUBLIC".equalsIgnoreCase(photo.getPrivacy())) {
            return true;
        }
        if (currentUser == null) {
            return false;
        }
        if (photo.getUserId().equals(currentUser.getId())) {
            return true;
        }
        if ("ADMIN".equalsIgnoreCase(currentUser.getRole()) || "MODERATOR".equalsIgnoreCase(currentUser.getRole())) {
            return true;
        }
        if ("FRIENDS".equalsIgnoreCase(photo.getPrivacy())) {
            return friendService.areFriends(currentUser.getId(), photo.getUserId());
        }
        return false;
    }

    private PhotoDto convertToDto(Photo photo) {
        PhotoDto dto = new PhotoDto();
        dto.setId(photo.getId());
        dto.setFilePath("/uploads/" + Paths.get(photo.getFilePath()).getFileName().toString());
        dto.setThumbnailPath(photo.getThumbnailPath());
        dto.setAuthorId(photo.getUserId());
        userRepository.findById(photo.getUserId())
                .ifPresent(u -> dto.setAuthorUsername(u.getUsername()));
        dto.setLikes(ratingRepository.countLikes(photo.getId()));
        dto.setDislikes(ratingRepository.countDislikes(photo.getId()));
        dto.setTags(tagRepository.findByPhotoId(photo.getId())
                .stream().map(Tag::getName).collect(Collectors.toList()));
        dto.setPrivacy(photo.getPrivacy());
        dto.setCreatedAt(photo.getCreatedAt());
        return dto;
    }
}