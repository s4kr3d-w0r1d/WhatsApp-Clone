package com.whatsappclone.controllers;

import com.whatsappclone.models.GroupChat;
import com.whatsappclone.services.GroupService;
import com.whatsappclone.dto.GroupCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupChatController {

    private final GroupService groupChatService;

    @Autowired
    public GroupChatController(GroupService groupChatService) {
        this.groupChatService = groupChatService;
    }

    // Create a new group. The GroupCreationRequest contains the group name and a set of usernames to be added as members.
    @PostMapping
    public ResponseEntity<GroupChat> createGroup(@RequestBody GroupCreationRequest request) {
        GroupChat createdGroup = groupChatService.createGroup(request.getOwnerId(), request.getName(),request.getDescription());
        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    // Add a new user to an existing group.
    @PostMapping("/{groupId}/users")
    public ResponseEntity<GroupChat> addUserToGroup(@PathVariable Long groupId, @RequestBody Long user_Id) {
        GroupChat updatedGroup = groupChatService.addMember(groupId, user_Id).getGroupChat();
        return ResponseEntity.ok(updatedGroup);
    }

    // Remove a user from an existing group.
    @DeleteMapping("/{groupId}/users/{user_id}")
    public ResponseEntity<GroupChat> removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long user_id) {
        GroupChat updatedGroup = groupChatService.removeMember(groupId, user_id);
        return ResponseEntity.ok(updatedGroup);
    }
}
