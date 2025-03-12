package com.whatsappclone.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GroupMessageDTO {
    private Long senderId;
    private String content;
    private String mediaUrl;  // URL of the uploaded media
    private String mediaType; // e.g., "image", "video", etc.

    public GroupMessageDTO() {}

    public GroupMessageDTO(Long senderId, String content, String mediaUrl, String mediaType) {
        this.senderId = senderId;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

}
