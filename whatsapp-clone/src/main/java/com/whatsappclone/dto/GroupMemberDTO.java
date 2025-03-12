package com.whatsappclone.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
@Setter
@Getter
@Data
public class GroupMemberDTO {
    // Getters and setters
    private Long id;
    private Long groupId;
    private String groupName;
    private Long userId;
    private String userName;

    public GroupMemberDTO() {
    }

    public GroupMemberDTO(Long id, Long groupId, String groupName, Long userId, String userName) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.userId = userId;
        this.userName = userName;
    }

}
