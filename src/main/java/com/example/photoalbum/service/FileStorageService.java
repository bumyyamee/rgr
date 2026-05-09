/*
package com.example.photoalbum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    */
/**
     * Сохраняет загруженный файл на диск и возвращает уникальное имя сохранённого файла.
     *//*

    public String store(MultipartFile file) {
        try {
            Path dir = Paths.get(uploadDir);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedFilename = UUID.randomUUID().toString() + extension;

            Path targetPath = dir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return storedFilename;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл: " + file.getOriginalFilename(), e);
        }
    }

    */
/**
     * Создаёт копию файла с новым именем. Возвращает имя нового файла.
     *//*

    public String copy(String filename) {
        try {
            Path source = Paths.get(uploadDir, filename);
            if (!Files.exists(source)) {
                throw new RuntimeException("Исходный файл не найден: " + filename);
            }

            String extension = "";
            int dotIndex = filename.lastIndexOf(".");
            if (dotIndex > 0) {
                extension = filename.substring(dotIndex);
            }
            String newFilename = UUID.randomUUID().toString() + extension;
            Path target = Paths.get(uploadDir, newFilename);
            Files.copy(source, target);
            return newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось скопировать файл: " + filename, e);
        }
    }

    */
/**
     * Удаляет файл с диска.
     *//*

    public void delete(String filename) {
        try {
            Path filePath = Paths.get(uploadDir, filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Игнорируем ошибку при удалении (файл может быть уже удалён)
        }
    }
}*/
