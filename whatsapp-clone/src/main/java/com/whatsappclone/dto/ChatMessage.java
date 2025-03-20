package com.whatsappclone.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
// This DTO represents a chat message exchanged over websockets.
@Data
public class ChatMessage {
    private Long senderId;
    private String content;
    // For media messages, you can optionally set the media URL and type.
    private String mediaUrl;
    private String mediaType;
    private Date timestamp;
}
