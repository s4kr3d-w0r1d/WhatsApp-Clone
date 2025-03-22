//package com.whatsappclone.dto;
//
//import lombok.*;
//
//@Data
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class ChatMessageWs {
//    private Long senderId;
//    private Long recipientId;
//    private Long groupId;
//    private String content;
//    private String mediaType;
//    private String mediaUrl;
////    private boolean encrypted;
//
//}

package com.whatsappclone.dto;

public class ChatMessageWs {
    private Long senderId;
    private Long recipientId;
    private String content;

    public ChatMessageWs() {}

    public ChatMessageWs(Long senderId, Long recipientId, String content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
    }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}

