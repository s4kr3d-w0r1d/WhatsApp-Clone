package com.whatsappclone.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class TypingIndicator {
    private Long chatId;   // Group ID or Private Chat ID
    private Long userId;   // The user who is typing
    private boolean typing; // True = typing, False = stopped

    public TypingIndicator() {}

    public TypingIndicator(Long chatId, Long userId, boolean typing) {
        this.chatId = chatId;
        this.userId = userId;
        this.typing = typing;
    }

}
