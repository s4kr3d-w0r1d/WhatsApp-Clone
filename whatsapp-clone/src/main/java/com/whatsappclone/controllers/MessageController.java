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

    // POST endpoint to send a one-to-one text message.
    @PostMapping
    public ResponseEntity<Message> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long recipientId,
            @RequestParam String content) {
        Message message = messageService.sendMessage(senderId, recipientId, content);
        return ResponseEntity.ok(message);
    }

    // POST endpoint to send a one-to-one media message.
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

    // GET endpoint to retrieve one-to-one chat history.
    @GetMapping
    public ResponseEntity<List<Message>> getChatHistory(
            @RequestParam Long userId1,
            @RequestParam Long userId2) {
        List<Message> messages = messageService.getChatHistory(userId1, userId2);
        return ResponseEntity.ok(messages);
    }

    // GET endpoint to retrieve group chat history.
    @GetMapping("/group/{groupId}/history")
    public ResponseEntity<List<Message>> getGroupChatHistory(@PathVariable Long groupId) {
        List<Message> messages = messageService.getGroupChatHistory(groupId);
        System.out.println("Found " + messages.size() + " messages for group " + groupId);
        return ResponseEntity.ok(messages);
    }
    @PostMapping("/group")
    public ResponseEntity<Message> sendGroupMessage(
            @RequestParam Long senderId,
            @RequestParam Long groupId,
            @RequestParam String content,
            // Optionally you can accept media parameters if needed.
            @RequestParam(required = false) String mediaUrl,
            @RequestParam(required = false) String mediaType) {
        Message message = messageService.sendGroupMessage(senderId, groupId, content, mediaUrl, mediaType);
        return ResponseEntity.ok(message);
    }


    // Endpoint to mark a message as delivered.
    @PostMapping("/{messageId}/delivered")
    public ResponseEntity<Message> markMessageAsDelivered(@PathVariable Long messageId) {
        Message updatedMessage = messageService.markMessageAsDelivered(messageId);
        return ResponseEntity.ok(updatedMessage);
    }

    // Endpoint to mark a message as read.
    @PostMapping("/{messageId}/read")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable Long messageId) {
        Message updatedMessage = messageService.markMessageAsRead(messageId);
        return ResponseEntity.ok(updatedMessage);
    }
}
