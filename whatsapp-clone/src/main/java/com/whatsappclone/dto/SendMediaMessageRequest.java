package com.whatsappclone.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMediaMessageRequest {
    private Long senderId;
    private Long recipientId;
    private String content;
    private String mediaUrl;
    private String mediaType;
}
