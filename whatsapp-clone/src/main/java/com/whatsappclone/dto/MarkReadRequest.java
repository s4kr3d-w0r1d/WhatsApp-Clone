package com.whatsappclone.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkReadRequest {
    private Long messageId;
    private Long senderId;
}
