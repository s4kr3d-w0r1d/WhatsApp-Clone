package com.whatsappclone.controllers;

import com.whatsappclone.models.*;
import com.whatsappclone.services.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupChatController {

    private final GroupService groupService;

    public GroupChatController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/search")
    public List<GroupChat> searchPublicGroups(@RequestParam String name) {
        return groupService.searchPublicGroups(name);
    }

    // Create a new group.
    @PostMapping("/create")
    public ResponseEntity<GroupChat> createGroup(
            @RequestParam Long ownerId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String groupType,
            @RequestParam(value = "groupPicture", required = false) MultipartFile groupPicture) {

        System.out.println("🟢 Request received: ownerId=" + ownerId + ", name=" + name + ", groupType=" + groupType);

        // Check if file is actually received
        if (groupPicture == null || groupPicture.isEmpty()) {
            System.err.println("❌ Error: groupPicture is NULL or Empty - No file uploaded.");
        } else {
            System.out.println("✅ Received file: " + groupPicture.getOriginalFilename() + " (" + groupPicture.getSize() + " bytes)");
        }

        // Convert groupType to enum
        GroupType parsedGroupType;
        try {
            parsedGroupType = GroupType.valueOf(groupType.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid GroupType: " + groupType);
            return ResponseEntity.badRequest().build();
        }

        // Call service layer
        GroupChat createdGroup = groupService.createGroup(ownerId, name, description, parsedGroupType, groupPicture);
        System.out.println("✅ Group created successfully: " + createdGroup.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
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
