package com.whatsappclone.controllers;

import com.whatsappclone.dto.GroupCreationRequest;
import com.whatsappclone.dto.GroupMemberDTO;
import com.whatsappclone.models.GroupChat;
import com.whatsappclone.services.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupChatController {

    private final GroupService groupService;

    public GroupChatController(GroupService groupService) {
        this.groupService = groupService;
    }

    // Endpoint to create a new group.
    @PostMapping
    public ResponseEntity<GroupChat> createGroup(@RequestBody GroupCreationRequest request) {
        GroupChat group = groupService.createGroup(request.getOwnerId(), request.getName(), request.getDescription());
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    // Endpoint to add a user to a group (only group owner can add).
    @PostMapping("/{groupId}/users")
    public ResponseEntity<GroupChat> addUserToGroup(
            @PathVariable Long groupId,
            @RequestParam Long ownerId,
            @RequestParam Long userId) {
        GroupChat group = groupService.addMember(groupId, ownerId, userId);
        return ResponseEntity.ok(group);
    }

    // Endpoint to remove a user from a group (only group owner can remove).
    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<GroupChat> removeUserFromGroup(
            @PathVariable Long groupId,
            @RequestParam Long ownerId,
            @PathVariable Long userId) {
        GroupChat group = groupService.removeMember(groupId, ownerId, userId);
        return ResponseEntity.ok(group);
    }

    // Endpoint to get the list of group members as DTOs.
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberDTO>> getGroupMembers(@PathVariable Long groupId) {
        List<GroupMemberDTO> members = groupService.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }
}
