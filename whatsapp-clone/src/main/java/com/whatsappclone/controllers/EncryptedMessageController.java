package com.whatsappclone.controllers;

import com.whatsappclone.models.Message;
import com.whatsappclone.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/encrypted-messages")
public class EncryptedMessageController {

    private final MessageService messageService;

    public EncryptedMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Send an E2E-encrypted message
    @PostMapping("/send")
    public ResponseEntity<Long> sendEncryptedMessage(
            @RequestParam Long senderId,
            @RequestParam Long recipientId,
            @RequestParam String plaintext
    ) throws Exception {
        Message msg = messageService.sendEncryptedMessage(senderId, recipientId, plaintext);
        return ResponseEntity.ok(msg.getId());
    }

    // Decrypt a message
    @GetMapping("/decrypt/{messageId}")
    public ResponseEntity<String> decryptMessage(
            @PathVariable Long messageId,
            @RequestParam Long viewerId
    ) throws Exception {
        String decrypted = messageService.decryptMessage(messageId, viewerId);
        return ResponseEntity.ok(decrypted);
    }
}

