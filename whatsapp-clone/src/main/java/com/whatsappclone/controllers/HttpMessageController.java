package com.whatsappclone.controllers;

import com.whatsappclone.dto.ChatSearchResponse;
import com.whatsappclone.models.Message;
import com.whatsappclone.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class HttpMessageController {

    private final MessageService messageService;

    public HttpMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Retrieve one-to-one chat history
    @GetMapping("/chat-history")
    public ResponseEntity<List<Message>> getChatHistory(@RequestParam Long userId1, @RequestParam Long userId2) {
        List<Message> messages = messageService.getChatHistory(userId1, userId2);
        return ResponseEntity.ok(messages);
    }

    // Retrieve group chat history
    @GetMapping("/group-history/{groupId}")
    public ResponseEntity<List<Message>> getGroupChatHistory(@PathVariable Long groupId) {
        List<Message> messages = messageService.getGroupChatHistory(groupId);
        return ResponseEntity.ok(messages);
    }

    // Search chats and profile by username
    @GetMapping("/search")
    public ResponseEntity<ChatSearchResponse> searchChatsAndProfile(@RequestParam Long searcherId,
                                                                    @RequestParam String searchedName) {
        ChatSearchResponse response = messageService.searchChatsAndProfile(searcherId, searchedName);
        return ResponseEntity.ok(response);
    }

    // Decrypt message for viewer
    @PostMapping("/decrypt-message")
    public ResponseEntity<String> decryptMessage(@RequestParam Long messageId, @RequestParam Long viewerId) throws Exception {
        String decryptedContent = messageService.decryptMessage(messageId, viewerId);
        return ResponseEntity.ok(decryptedContent);
    }
}
