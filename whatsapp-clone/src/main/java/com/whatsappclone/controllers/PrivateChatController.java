package com.whatsappclone.controllers;

import com.whatsappclone.models.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class PrivateChatController {
    private final SimpMessagingTemplate messagingTemplate;

    public PrivateChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

//    @MessageMapping("/private/{recipient}")
//    public void sendPrivateMessage(ChatMessage message, @org.springframework.messaging.handler.annotation.DestinationVariable String recipient) {
//        messagingTemplate.convertAndSendToUser(recipient,"queue/messages",message);
//    }

//    @MessageMapping("/private/{recipientId}")
//    @SendToUser("/queue/messages")
//    public String sendPrivateMessage(@DestinationVariable String recipientId, String message) {
//        return message;
//    }

    @MessageMapping("/app/private/{recipientId}")
    public void sendPrivateMessage(@DestinationVariable String recipientId, ChatMessage message) {
        messagingTemplate.convertAndSendToUser(recipientId, "/queue/messages", message);
    }
}
