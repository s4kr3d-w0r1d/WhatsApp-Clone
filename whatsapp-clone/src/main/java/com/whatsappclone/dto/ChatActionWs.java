package com.whatsappclone.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatActionWs {
    // Supported actions: "deleteForMe", "deleteForEveryone", "delivered", "read"
    private String action;
    private Long messageId;
    // The user performing the action.
    private Long userId;

}
