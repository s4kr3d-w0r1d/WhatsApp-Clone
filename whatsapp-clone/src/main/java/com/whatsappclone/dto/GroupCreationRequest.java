package com.whatsappclone.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class GroupCreationRequest {
    // Getters and setters
    private Long ownerId;
    private String name;
    private String description;

}
