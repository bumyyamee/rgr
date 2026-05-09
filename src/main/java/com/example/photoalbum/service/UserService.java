/*
package com.example.photoalbum.service;

import com.example.photoalbum.dto.RegisterRequest;
import com.example.photoalbum.dto.UserDto;
import com.example.photoalbum.exception.ResourceNotFoundException;
import com.example.photoalbum.model.User;
import com.example.photoalbum.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new RuntimeException("Username already exists");
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        Long id = userRepository.save(user);
        user.setId(id);
        emailService.sendConfirmationEmail(user);
        return user;
    }

    public UserDto getUserById(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDto(u);
    }

    public UserDto getCurrentUser(String username) {
        User u = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDto(u);
    }

    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getId();
    }

//    public List<UserDto> getAllUsers(int page, int size) {
//        return userRepository.findAll(page, size).stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    public void updateRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(role.toUpperCase());
        userRepository.update(user);
    }

    public void updateProfile(Long userId, UserDto dto, String username) {
        User current = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!current.getUsername().equals(username) && !current.getRole().equals("ADMIN")) {
            throw new RuntimeException("Access denied");
        }
        current.setEmail(dto.getEmail());
        current.setUsername(dto.getUsername());
        userRepository.update(current);
    }

    private UserDto convertToDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setAvatarPath(u.getAvatarPath());
        return dto;
    }
}*/
