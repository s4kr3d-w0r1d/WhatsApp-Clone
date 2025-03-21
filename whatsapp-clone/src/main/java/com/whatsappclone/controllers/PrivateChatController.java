package com.whatsappclone.controllers;

import com.whatsappclone.models.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PrivateChatController {
    private final SimpMessagingTemplate messagingTemplate;

    public PrivateChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/app/private/{recipientId}")
    public void sendPrivateMessage(@DestinationVariable String recipientId, ChatMessage message) {
        messagingTemplate.convertAndSendToUser(recipientId, "/queue/messages", message);
    }
}
