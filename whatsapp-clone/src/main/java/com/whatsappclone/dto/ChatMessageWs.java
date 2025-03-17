package com.whatsappclone.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatMessageWs {
    private Long senderId;
    private Long recipientId;
    private Long groupId;
    private String content;
    private String mediaType;
    private String mediaUrl;
    private boolean encrypted;
}
