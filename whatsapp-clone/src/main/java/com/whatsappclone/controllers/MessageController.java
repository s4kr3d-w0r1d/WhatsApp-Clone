package com.whatsappclone.controllers;

import com.whatsappclone.models.Message;
import com.whatsappclone.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // POST endpoint to send a message.
    @PostMapping
    public ResponseEntity<Message> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long recipientId,
            @RequestParam String content) {
        Message message = messageService.sendMessage(senderId, recipientId, content);
        return ResponseEntity.ok(message);
    }

    // New endpoint to send a media message
    @PostMapping("/media")
    public ResponseEntity<Message> sendMediaMessage(
            @RequestParam Long senderId,
            @RequestParam Long recipientId,
            @RequestParam(required = false) String content,
            @RequestParam MultipartFile mediaFile,
            @RequestParam String mediaType) {
        Message message = messageService.sendMediaMessage(senderId, recipientId, content, mediaFile, mediaType);
        return ResponseEntity.ok(message);
    }

    // GET endpoint to retrieve chat history between two users.
    @GetMapping
    public ResponseEntity<List<Message>> getChatHistory(
            @RequestParam Long userId1,
            @RequestParam Long userId2) {
        List<Message> messages = messageService.getChatHistory(userId1, userId2);
        return ResponseEntity.ok(messages);
    }
}
