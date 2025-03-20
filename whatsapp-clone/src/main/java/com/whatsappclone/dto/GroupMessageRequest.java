package com.whatsappclone.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageRequest {
    private Long senderId;
    private Long groupId;
    private String content;
    private String mediaUrl;
    private String mediaType;
}
