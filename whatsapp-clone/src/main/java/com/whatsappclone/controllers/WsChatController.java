package com.whatsappclone.controllers;

import com.whatsappclone.dto.*;
import com.whatsappclone.models.Message;
import com.whatsappclone.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WsChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public WsChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    // Send a one-to-one text message in real-time
    @MessageMapping("/send-message")
    public void sendMessage(SendMessageRequest request) {
        Message message = messageService.sendMessage(request.getSenderId(), request.getRecipientId(), request.getContent());
        // Broadcast to the recipient's topic (for example: /topic/messages/{recipientId})
        messagingTemplate.convertAndSend("/topic/messages/" + request.getRecipientId(), message);
    }

    // Send a media message in real-time
    @MessageMapping("/send-media-message")
    public void sendMediaMessage(SendMediaMessageRequest request) {
        Message message = messageService.sendMediaMessage(
                request.getSenderId(),
                request.getRecipientId(),
                request.getContent(),
                request.getMediaUrl(),  // Media URL instead of file
                request.getMediaType()
        );
        messagingTemplate.convertAndSend("/topic/messages/" + request.getRecipientId(), message);
    }

    // Send a group message
    @MessageMapping("/send-group-message")
    public void sendGroupMessage(GroupMessageRequest request) {
        Message message = messageService.sendGroupMessage(
                request.getSenderId(),
                request.getGroupId(),
                request.getContent(),
                request.getMediaUrl(),
                request.getMediaType()
        );
        messagingTemplate.convertAndSend("/topic/group/" + request.getGroupId(), message);
    }

    // Mark a message as delivered
    @MessageMapping("/mark-delivered")
    public void markMessageAsDelivered(MarkDeliveredRequest request) {
        Message message = messageService.markMessageAsDelivered(request.getMessageId());
        messagingTemplate.convertAndSend("/topic/messages/" + request.getRecipientId(), message);
    }

    // Mark a message as read
    @MessageMapping("/mark-read")
    public void markMessageAsRead(MarkReadRequest request) {
        Message message = messageService.markMessageAsRead(request.getMessageId());
        messagingTemplate.convertAndSend("/topic/messages/" + request.getSenderId(), message);
    }

    // Delete a message for a single user
    @MessageMapping("/delete-message-user")
    public void deleteMessageForUser(DeleteMessageRequest request) {
        messageService.deleteMessageForUser(request.getMessageId(), request.getUserId());
        messagingTemplate.convertAndSend("/topic/messages/" + request.getUserId(), "Message deleted");
    }

    // Delete a message for everyone
    @MessageMapping("/delete-message-everyone")
    public void deleteMessageForEveryone(DeleteMessageRequest request) {
        messageService.deleteMessageForEveryone(request.getMessageId(), request.getUserId());
        messagingTemplate.convertAndSend("/topic/messages", "Message deleted for everyone");
    }

    // Send an encrypted message in real-time
    @MessageMapping("/send-encrypted-message")
    public void sendEncryptedMessage(EncryptedMessageRequest request) throws Exception {
        Message message = messageService.sendEncryptedMessage(request.getSenderId(), request.getRecipientId(), request.getPlaintext());
        messagingTemplate.convertAndSend("/topic/messages/" + request.getRecipientId(), message);
    }
}
