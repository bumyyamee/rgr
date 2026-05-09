/*
package com.example.photoalbum.controller;

import com.example.photoalbum.dto.CommentDto;
import com.example.photoalbum.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos/{photoId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long photoId) {
        return ResponseEntity.ok(commentService.getCommentsByPhotoId(photoId));
    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@PathVariable Long photoId,
                                                 @RequestBody String content,
                                                 Authentication auth) {
        return ResponseEntity.ok(commentService.addComment(photoId, content, auth.getName()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long photoId,
                                           @PathVariable Long commentId,
                                           Authentication auth) {
        commentService.deleteComment(commentId, auth.getName());
        return ResponseEntity.ok("Comment deleted");
    }
}*/
