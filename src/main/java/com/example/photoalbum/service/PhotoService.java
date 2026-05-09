/*
package com.example.photoalbum.service;

import com.example.photoalbum.dto.PhotoDto;
import com.example.photoalbum.exception.ResourceNotFoundException;
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

    public PhotoService(PhotoRepository photoRepository,
                        AlbumRepository albumRepository,
                        TagRepository tagRepository,
                        RatingRepository ratingRepository,
                        UserRepository userRepository,
                        FileStorageService fileStorageService,
                        EmailService emailService) {
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
        this.tagRepository = tagRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.emailService = emailService;
    }

    */
/**
     * Загрузка фото в альбом.
     *//*

    @Transactional
    public PhotoDto upload(MultipartFile file, Long albumId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        var album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Альбом не найден"));

        if (!album.getUserId().equals(user.getId())) {
            throw new RuntimeException("Вы не можете добавлять фото в чужой альбом");
        }

        String storedFilename = fileStorageService.store(file);

        Photo photo = new Photo();
        photo.setUserId(user.getId());
        photo.setFilePath(storedFilename);
        Long photoId = photoRepository.save(photo);
        photo.setId(photoId);

        albumRepository.addPhoto(albumId, photoId);

        return convertToDto(photo);
    }

    */
/**
     * Получить ленту фотографий.
     * В данной упрощённой реализации возвращаются все фотографии, доступные текущему пользователю.
     *//*

    public List<PhotoDto> getFeed(String username, String privacy, List<String> tags,
                                  String dateFrom, String dateTo, int page, int size) {
        // В реальном приложении здесь должна быть сложная фильтрация по приватности, тегам, датам.
        // Для примера возвращаем все фото из публичных альбомов.
        List<Photo> photos = photoRepository.findPublicFeed(null, null, null, null, page, size);
        return photos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    */
/**
     * Получить фотографию по ID.
     *//*

    public PhotoDto getPhotoById(Long id, String username) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фото не найдено"));
        // Здесь должна быть проверка прав доступа (приватность альбома)
        return convertToDto(photo);
    }

    */
/**
     * Удалить фотографию (доступно владельцу, модератору, админу).
     *//*

    @Transactional
    public void deletePhoto(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фото не найдено"));

        if (!photo.getUserId().equals(user.getId())
                && !user.getRole().equals("ADMIN")
                && !user.getRole().equals("MODERATOR")) {
            throw new RuntimeException("У вас нет прав на удаление этого фото");
        }

        // Удаляем физический файл
        fileStorageService.delete(photo.getFilePath());
        // Удаляем запись из БД (каскадом удалятся связи)
        photoRepository.delete(id);
    }

    */
/**
     * Копировать фото в свой альбом.
     *//*

    @Transactional
    public PhotoDto copyPhoto(Long photoId, Long targetAlbumId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Photo original = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Исходное фото не найдено"));

        var targetAlbum = albumRepository.findById(targetAlbumId)
                .orElseThrow(() -> new ResourceNotFoundException("Целевой альбом не найден"));

        if (!targetAlbum.getUserId().equals(user.getId())) {
            throw new RuntimeException("Вы не можете копировать в чужой альбом");
        }

        // Копируем файл
        String newFilename = fileStorageService.copy(original.getFilePath());

        Photo newPhoto = new Photo();
        newPhoto.setUserId(user.getId());
        newPhoto.setFilePath(newFilename);
        Long newPhotoId = photoRepository.save(newPhoto);
        newPhoto.setId(newPhotoId);

        albumRepository.addPhoto(targetAlbumId, newPhotoId);

        return convertToDto(newPhoto);
    }

    */
/**
     * Оценить фотографию (1 - лайк, -1 - дизлайк).
     *//*

    @Transactional
    public void ratePhoto(Long photoId, String username, int value) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Фото не найдено"));

        Optional<Rating> existingRating = ratingRepository.findByUserAndPhoto(user.getId(), photoId);
        if (existingRating.isPresent()) {
            Rating r = existingRating.get();
            if (r.getValue() == value) {
                // Повторное нажатие — удаляем оценку
                ratingRepository.delete(r.getId());
            } else {
                // Меняем оценку
                r.setValue(value);
                ratingRepository.update(r);
            }
        } else {
            Rating r = new Rating();
            r.setUserId(user.getId());
            r.setPhotoId(photoId);
            r.setValue(value);
            ratingRepository.save(r);

            // Отправляем уведомление владельцу (если это не сам оценивший)
            if (!photo.getUserId().equals(user.getId())) {
                User owner = userRepository.findById(photo.getUserId()).orElse(null);
                if (owner != null) {
                    emailService.sendRatingNotification(owner, photoId.toString(), user.getUsername(), value);
                }
            }
        }
    }

    */
/**
     * Преобразование Photo в PhotoDto.
     *//*

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
        dto.setCreatedAt(photo.getCreatedAt());
        return dto;
    }
}*/
