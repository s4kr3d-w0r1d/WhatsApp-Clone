package com.whatsappclone.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String sender;
    private String content;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
