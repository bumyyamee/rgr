package com.example.photoalbum.service;

import com.example.photoalbum.dto.RegisterRequest;
import com.example.photoalbum.dto.UserDto;
import com.example.photoalbum.exception.ResourceNotFoundException;
import com.example.photoalbum.model.User;
import com.example.photoalbum.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDto(u);
    }

    // по почтее
    public UserDto getCurrentUser(String email) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertToDto(u);
    }

    // по почте ищем а не по нику
    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email))
                .getId();
    }

    
    public List<UserDto> getAllUsers(int page, int size) {
        return userRepository.findAll(page, size).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void updateRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(role.toUpperCase().replaceAll("\"", ""));
        userRepository.update(user);
    }

    // по почте сравниваем а не нику 
    public void updateProfile(Long userId, UserDto dto, String email) {
        User current = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!current.getEmail().equals(email) && !current.getRole().equals("ADMIN")) {
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
}