package com.whatsappclone.dto;

import lombok.Data;
import java.util.Set;

@Data
public class GroupCreationRequest {
    private Long ownerId;
    private String name;
    private String description;
}
