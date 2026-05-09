/*
package com.example.photoalbum.service;

import com.example.photoalbum.exception.ResourceNotFoundException;
import com.example.photoalbum.model.Friendship;
import com.example.photoalbum.model.User;
import com.example.photoalbum.repository.FriendshipRepository;
import com.example.photoalbum.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendService(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void sendFriendRequest(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) throw new RuntimeException("Cannot friend yourself");
        // проверяем, существует ли уже запрос
        friendshipRepository.findByUserAndFriend(fromUserId, toUserId).ifPresent(f -> {
            throw new RuntimeException("Friendship already exists or request sent");
        });
        Friendship friendship = new Friendship();
        friendship.setUserId(fromUserId);
        friendship.setFriendId(toUserId);
        friendship.setStatus("PENDING");
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void acceptRequest(Long requestId, Long currentUserId) {
        Friendship friendship = friendshipRepository.findByUserAndFriend(requestId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship request not found"));
        if (!friendship.getFriendId().equals(currentUserId))
            throw new RuntimeException("Not your request to accept");
        if (!"PENDING".equals(friendship.getStatus()))
            throw new RuntimeException("Request is not pending");
        friendshipRepository.updateStatus(friendship.getId(), "ACCEPTED");
    }

    public List<User> getFriends(Long userId) {
        List<Friendship> friendships = friendshipRepository.getFriends(userId);
        return friendships.stream()
                .map(f -> {
                    Long friendId = f.getUserId().equals(userId) ? f.getFriendId() : f.getUserId();
                    return userRepository.findById(friendId).orElse(null);
                })
                .filter(u -> u != null)
                .collect(Collectors.toList());
    }

    public boolean areFriends(Long userId1, Long userId2) {
        return friendshipRepository.findByUserAndFriend(userId1, userId2)
                .filter(f -> "ACCEPTED".equals(f.getStatus()))
                .isPresent() ||
                friendshipRepository.findByUserAndFriend(userId2, userId1)
                        .filter(f -> "ACCEPTED".equals(f.getStatus()))
                        .isPresent();
    }
}*/
