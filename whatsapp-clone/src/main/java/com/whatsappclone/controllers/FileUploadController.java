package com.whatsappclone.controllers;

import com.whatsappclone.services.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/messages")
public class FileUploadController {

    @Value("${uploads.chat}")
    private String chatUploadPath; // e.g., "C:/uploads/chat" or "/var/uploads/chat"

    // Use a dedicated service for file storage if desired.
    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload-media")
    public ResponseEntity<String> uploadMedia(@RequestParam("file") MultipartFile file) {
        // Save the file and return the HTTP-accessible URL.
        String fileUrl = fileStorageService.storeFile(file, chatUploadPath);
        // Return the full URL (adjust the domain/port as necessary)
        return ResponseEntity.ok("http://localhost:8080" + fileUrl);
    }
}
