package com.whatsappclone.controllers;

import com.whatsappclone.dto.ChatActionWs;
import com.whatsappclone.dto.ChatMessageWs;
import com.whatsappclone.models.Message;
import com.whatsappclone.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WsChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WsChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    // One-to-one private chat.
    @MessageMapping("/chat/private")
    public void handlePrivateChat(@Payload ChatMessageWs chatMessage) throws Exception {
        Message savedMessage;
        if (chatMessage.getMediaUrl() != null && !chatMessage.getMediaUrl().isEmpty()) {
            if (chatMessage.isEncrypted()) {
                savedMessage = messageService.sendEncryptedMediaMessageUrlWs(
                        chatMessage.getSenderId(),
                        chatMessage.getRecipientId(),
                        chatMessage.getContent(),
                        chatMessage.getMediaUrl(),
                        chatMessage.getMediaType()
                );
            } else {
                savedMessage = messageService.sendMediaMessageUrlWs(
                        chatMessage.getSenderId(),
                        chatMessage.getRecipientId(),
                        chatMessage.getContent(),
                        chatMessage.getMediaUrl(),
                        chatMessage.getMediaType()
                );
            }
        } else {
            if (chatMessage.isEncrypted()) {
                savedMessage = messageService.sendEncryptedMessage(
                        chatMessage.getSenderId(),
                        chatMessage.getRecipientId(),
                        chatMessage.getContent()
                );
            } else {
                savedMessage = messageService.sendMessage(
                        chatMessage.getSenderId(),
                        chatMessage.getRecipientId(),
                        chatMessage.getContent()
                );
            }
        }
        // Deliver the message to the recipient's WebSocket queue.
        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()),
                "/queue/messages",
                savedMessage
        );
    }

    // Group chat.
    @MessageMapping("/chat/group")
    public void handleGroupChat(@Payload ChatMessageWs chatMessage) throws Exception {
        Message savedMessage;
        if (chatMessage.getMediaUrl() != null && !chatMessage.getMediaUrl().isEmpty()) {
            if (chatMessage.isEncrypted()) {
                savedMessage = messageService.sendEncryptedGroupMediaMessageUrlWs(
                        chatMessage.getSenderId(),
                        chatMessage.getGroupId(),
                        chatMessage.getContent(),
                        chatMessage.getMediaUrl(),
                        chatMessage.getMediaType()
                );
            } else {
                savedMessage = messageService.sendGroupMediaMessageUrlWs(
                        chatMessage.getSenderId(),
                        chatMessage.getGroupId(),
                        chatMessage.getContent(),
                        chatMessage.getMediaUrl(),
                        chatMessage.getMediaType()
                );
            }
        } else {
            if (chatMessage.isEncrypted()) {
                savedMessage = messageService.sendEncryptedGroupMessage(
                        chatMessage.getSenderId(),
                        chatMessage.getGroupId(),
                        chatMessage.getContent()
                );
            } else {
                savedMessage = messageService.sendGroupMessage(
                        chatMessage.getSenderId(),
                        chatMessage.getGroupId(),
                        chatMessage.getContent(),
                        null,
                        null
                );
            }
        }
        // Broadcast the group message.
        messagingTemplate.convertAndSend("/topic/group/" + chatMessage.getGroupId(), savedMessage);
    }

    // Handle message actions: delete, delivered, and read.
    @MessageMapping("/chat/action")
    public void handleMessageAction(@Payload ChatActionWs action) {
        Message updatedMessage = null;
        switch (action.getAction()) {
            case "deleteForMe":
                messageService.deleteMessageForUser(action.getMessageId(), action.getUserId());
                break;
            case "deleteForEveryone":
                messageService.deleteMessageForEveryone(action.getMessageId(), action.getUserId());
                break;
            case "delivered":
                updatedMessage = messageService.markMessageAsDelivered(action.getMessageId());
                break;
            case "read":
                updatedMessage = messageService.markMessageAsRead(action.getMessageId());
                break;
            default:
                throw new RuntimeException("Unsupported action: " + action.getAction());
        }
        // Optionally, notify both sender and recipient about status changes.
        if (updatedMessage != null) {
            if (updatedMessage.getRecipient() != null) {
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(updatedMessage.getRecipient().getId()),
                        "/queue/status",
                        updatedMessage
                );
            }
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(updatedMessage.getSender().getId()),
                    "/queue/status",
                    updatedMessage
            );
        }
    }
}
