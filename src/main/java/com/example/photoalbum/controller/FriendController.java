package com.example.photoalbum.controller;

import com.example.photoalbum.model.User;
import com.example.photoalbum.service.FriendService;
import com.example.photoalbum.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    public FriendController(FriendService friendService, UserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }

    @PostMapping("/request/{toUserId}")
    public ResponseEntity<?> sendRequest(@PathVariable Long toUserId, Authentication auth) {
        Long fromUserId = getCurrentUserId(auth);
        friendService.sendFriendRequest(fromUserId, toUserId);
        return ResponseEntity.ok("Request sent");
    }

    @PutMapping("/accept/{friendshipId}")
    public ResponseEntity<?> accept(@PathVariable Long friendshipId, Authentication auth) {
        Long currentUserId = getCurrentUserId(auth);
        friendService.acceptRequest(friendshipId, currentUserId);
        return ResponseEntity.ok("Request accepted");
    }

    @GetMapping
    public ResponseEntity<List<User>> getFriends(Authentication auth) {
        Long userId = getCurrentUserId(auth);
        return ResponseEntity.ok(friendService.getFriends(userId));
    }

    // упрощенный метод получения ID текущего пользователя; логика должна использовать UserService
    private Long getCurrentUserId(Authentication auth) {
        // Здесь нужно вызвать userService.getCurrentUser(auth.getName()).getId()
        // Чтобы не вносить циклическую зависимость, проще инжектить UserService.
        // Но в контроллер можно инжектить UserService.
        return userService.getCurrentUser(auth.getName()).getId();
    }
}