/*
package com.example.photoalbum.service;

import com.example.photoalbum.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(user.getEmail());
        message.setSubject("Добро пожаловать в Фотоальбом!");
        message.setText("Здравствуйте, " + user.getUsername() + "!\n\n"
                + "Ваш аккаунт успешно зарегистрирован.\n"
                + "С уважением, команда Фотоальбом.");
        mailSender.send(message);
    }

    public void sendCommentNotification(User photoOwner, String photoId, String commenterUsername) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(photoOwner.getEmail());
        message.setSubject("Новый комментарий к вашему фото #" + photoId);
        message.setText("Пользователь " + commenterUsername + " оставил комментарий к вашей фотографии.");
        mailSender.send(message);
    }

    public void sendRatingNotification(User photoOwner, String photoId, String raterUsername, int value) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(photoOwner.getEmail());
        String action = value == 1 ? "понравилась" : "не понравилась";
        message.setSubject("Новая оценка вашего фото #" + photoId);
        message.setText("Пользователю " + raterUsername + " " + action + " ваша фотография.");
        mailSender.send(message);
    }

    public void sendFriendRequestNotification(User toUser, String fromUsername) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toUser.getEmail());
        message.setSubject("Запрос в друзья от " + fromUsername);
        message.setText("Пользователь " + fromUsername + " хочет добавить вас в друзья.");
        mailSender.send(message);
    }
}*/
