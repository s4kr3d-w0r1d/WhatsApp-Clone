package com.whatsappclone.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkDeliveredRequest {
    private Long messageId;
    private Long recipientId;
}
