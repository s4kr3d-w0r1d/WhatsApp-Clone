package com.whatsappclone.controllers;

import com.whatsappclone.dto.GroupMessageDTO;
import com.whatsappclone.models.Message;
import com.whatsappclone.services.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GroupMessageController {

    private final MessageService messageService;

    public GroupMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/group/{groupId}")
    @SendTo("/topic/group.{groupId}")
    public Message sendGroupMessage(@DestinationVariable Long groupId, GroupMessageDTO dto) {
        return messageService.sendGroupMessage(
                dto.getSenderId(),
                groupId,
                dto.getContent(),
                dto.getMediaUrl(),   // media URL provided by client
                dto.getMediaType()   // media type (if any)
        );
    }
}
