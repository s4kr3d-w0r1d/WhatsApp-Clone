package com.whatsappclone.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
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
