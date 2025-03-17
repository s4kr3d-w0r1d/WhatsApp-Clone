package com.whatsappclone.controllers;

import com.whatsappclone.dto.GroupCreationRequest;
import com.whatsappclone.models.GroupChat;
import com.whatsappclone.models.GroupJoinRequest;
import com.whatsappclone.models.GroupMember;
import com.whatsappclone.models.GroupMemberRole;
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

    // Create a new group.
    @PostMapping
    public ResponseEntity<GroupChat> createGroup(@RequestBody GroupCreationRequest request) {
        GroupChat group = groupService.createGroup(
                request.getOwnerId(),
                request.getName(),
                request.getDescription(),
                request.getGroupType());
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    // For public groups: user sends a join request.
    @PostMapping("/{groupId}/join-request")
    public ResponseEntity<GroupJoinRequest> requestToJoin(@PathVariable Long groupId,
                                                          @RequestParam Long userId) {
        GroupJoinRequest request = groupService.requestToJoinGroup(userId, groupId);
        return ResponseEntity.ok(request);
    }

    // Admin approves a join request.
    @PostMapping("/{groupId}/join-request/{requestId}/approve")
    public ResponseEntity<GroupMember> approveJoinRequest(@PathVariable Long groupId,
                                                          @RequestParam Long adminId,
                                                          @PathVariable Long requestId) {
        GroupMember member = groupService.approveJoinRequest(groupId, adminId, requestId);
        return ResponseEntity.ok(member);
    }

    // For private groups: admin adds a member.
    @PostMapping("/{groupId}/users")
    public ResponseEntity<GroupMember> addMemberToGroup(@PathVariable Long groupId,
                                                        @RequestParam Long adminId,
                                                        @RequestParam Long userId) {
        GroupMember member = groupService.addMemberToPrivateGroup(groupId, adminId, userId);
        return ResponseEntity.ok(member);
    }

    // Remove a member or let a member leave.
    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId,
                                             @RequestParam Long removerId,
                                             @PathVariable Long userId) {
        groupService.removeMember(groupId, removerId, userId);
        return ResponseEntity.noContent().build();
    }

    // Retrieve all members of a group.
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMember>> getGroupMembers(@PathVariable Long groupId) {
        List<GroupMember> members = groupService.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }

    // Update a member's role (e.g., promote to admin, demote to member)
    @PutMapping("/{groupId}/users/{userId}/role")
    public ResponseEntity<GroupMember> updateMemberRole(@PathVariable Long groupId,
                                                        @RequestParam Long adminId,
                                                        @PathVariable Long userId,
                                                        @RequestParam GroupMemberRole newRole) {
        GroupMember member = groupService.updateMemberRole(groupId, adminId, userId, newRole);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<GroupChat> leaveGroup(@PathVariable Long groupId,
                                                @RequestParam Long userId) {
        GroupChat updatedGroup = groupService.leaveGroup(groupId, userId);
        return ResponseEntity.ok(updatedGroup);
    }
}
