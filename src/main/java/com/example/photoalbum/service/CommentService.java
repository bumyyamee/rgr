/*
package com.example.photoalbum.service;

import com.example.photoalbum.dto.CommentDto;
import com.example.photoalbum.exception.ResourceNotFoundException;
import com.example.photoalbum.model.Comment;
import com.example.photoalbum.model.Photo;
import com.example.photoalbum.model.User;
import com.example.photoalbum.repository.CommentRepository;
import com.example.photoalbum.repository.PhotoRepository;
import com.example.photoalbum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public CommentService(CommentRepository commentRepository, PhotoRepository photoRepository,
                          UserRepository userRepository, EmailService emailService) {
        this.commentRepository = commentRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public List<CommentDto> getCommentsByPhotoId(Long photoId) {
        return commentRepository.findByPhotoId(photoId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CommentDto addComment(Long photoId, String content, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));

        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setPhotoId(photoId);
        comment.setContent(content);
        commentRepository.save(comment);

        // уведомление владельцу фото
        User photoOwner = userRepository.findById(photo.getUserId()).orElse(null);
        if (photoOwner != null && !photoOwner.getId().equals(user.getId())) {
            emailService.sendCommentNotification(photoOwner, photoId.toString(), user.getUsername());
        }

        return toDto(comment);
    }

    public void deleteComment(Long commentId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!comment.getUserId().equals(user.getId()) && !user.getRole().equals("ADMIN") && !user.getRole().equals("MODERATOR")) {
            throw new RuntimeException("Not allowed to delete this comment");
        }
        commentRepository.delete(commentId);
    }

    private CommentDto toDto(Comment c) {
        CommentDto dto = new CommentDto();
        dto.setId(c.getId());
        dto.setContent(c.getContent());
        dto.setCreatedAt(c.getCreatedAt());
        userRepository.findById(c.getUserId())
                .ifPresent(u -> dto.setAuthorUsername(u.getUsername()));
        return dto;
    }
}*/
