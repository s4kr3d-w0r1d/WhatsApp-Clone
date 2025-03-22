package com.whatsappclone.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EncryptedMessageRequest {
    private Long senderId;
    private Long recipientId;
    private String plaintext;

}
