package com.whatsappclone.services;

import com.whatsappclone.models.*;
import com.whatsappclone.repositories.GroupChatRepository;
import com.whatsappclone.repositories.GroupJoinRequestRepository;
import com.whatsappclone.repositories.GroupMemberRepository;
import com.whatsappclone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupChatRepository groupChatRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupJoinRequestRepository joinRequestRepository;
    private final UserRepository userRepository;
    @Value("${uploads.path:C:/uploads}")  // Read upload path from properties file
    private String uploadPath;
    public GroupService(GroupChatRepository groupChatRepository,
                        GroupMemberRepository groupMemberRepository,
                        GroupJoinRequestRepository joinRequestRepository,
                        UserRepository userRepository) {
        this.groupChatRepository = groupChatRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.joinRequestRepository = joinRequestRepository;
        this.userRepository = userRepository;
    }

    public List<GroupChat> searchPublicGroups(String name) {
        return groupChatRepository.findByNameContainingIgnoreCaseAndGroupType(name, GroupType.PUBLIC);
    }
    // Create a new group. The creator becomes owner and admin.
    public GroupChat createGroup(Long ownerId, String name, String description, GroupType groupType, MultipartFile groupDp) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        GroupChat group = new GroupChat();
        group.setName(name);
        group.setDescription(description);
        group.setOwner(owner);
        group.setGroupType(groupType);
        group.setCreatedAt(new Date());

        // Debugging: Check if file is received
        if (groupDp == null) {
            System.err.println("Error: groupDp is NULL - No file was uploaded.");
        } else if (groupDp.isEmpty()) {
            System.err.println("Error: groupDp is EMPTY - File not received.");
        } else {
            System.out.println("Received File: " + groupDp.getOriginalFilename());
        }

        // Handle group DP upload if provided
        if (groupDp != null && !groupDp.isEmpty()) {
            String groupUploadPath = "C:/uploads/";  // Ensure this matches your setup
            String fileName = System.currentTimeMillis() + "_" + groupDp.getOriginalFilename();
            File uploadDir = new File(groupUploadPath);

            // Create directory if it doesn't exist
            if (!uploadDir.exists()) {
                boolean dirCreated = uploadDir.mkdirs();
                System.out.println("Creating directory: " + groupUploadPath + " → Success: " + dirCreated);
            }

            File dest = new File(uploadDir, fileName);
            try {
                groupDp.transferTo(dest);
                System.out.println("File uploaded successfully: " + dest.getAbsolutePath());

                group.setProfilePictureUrl("/uploads/" + fileName); // Store relative file path
            } catch (IOException e) {
                System.err.println("Failed to save group DP: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to save group DP", e);
            }
        } else {
            System.out.println("⚠️ No image uploaded. Using default DP.");
            group.setProfilePictureUrl("/uploads/groups/default-group-dp.png"); // Default group DP
        }

        GroupChat savedGroup = groupChatRepository.save(group);

        // Add owner as a member with ADMIN role
        GroupMember member = new GroupMember();
        member.setGroupChat(savedGroup);
        member.setUser(owner);
        member.setRole(GroupMemberRole.ADMIN);
        member.setJoinedAt(new Date());
        groupMemberRepository.save(member);

        return savedGroup;
    }



    // For public groups: a user can request to join.
    public GroupJoinRequest requestToJoinGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        if (group.getGroupType() != GroupType.PUBLIC) {
            throw new RuntimeException("This group is private. Only admins can add members.");
        }
        // Check if a join request already exists
        Optional<GroupJoinRequest> existingRequest = joinRequestRepository.findByGroupChatAndUser(group, user);
        if (existingRequest.isPresent()) {
            throw new RuntimeException("Join request already sent.");
        }
        GroupJoinRequest request = new GroupJoinRequest();
        request.setGroupChat(group);
        request.setUser(user);
        request.setRequestDate(new Date());
        request.setStatus(RequestStatus.PENDING);
        return joinRequestRepository.save(request);
    }

    @Transactional
    public GroupMember approveJoinRequest(Long groupId, Long adminId, Long requestId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        GroupMember adminMembership = groupMemberRepository.findByGroupChatAndUser(group, admin)
                .orElseThrow(() -> new RuntimeException("Admin is not a member of the group"));

        if (adminMembership.getRole() != GroupMemberRole.ADMIN) {
            throw new RuntimeException("Only admins can approve join requests.");
        }

        GroupJoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Join request not found"));

        // **Fix: Handle already processed requests properly**
        if (joinRequest.getStatus() == RequestStatus.APPROVED) {
            // Check if user is already a member
            if (groupMemberRepository.findByGroupChatAndUser(group, joinRequest.getUser()).isPresent()) {
                throw new RuntimeException("User is already a member of the group.");
            }
        } else if (joinRequest.getStatus() == RequestStatus.REJECTED) {
            throw new RuntimeException("Join request was rejected earlier.");
        }

        // Approve the request
        joinRequest.setStatus(RequestStatus.APPROVED);
        joinRequestRepository.save(joinRequest);

        // Add user as a member (if not already added)
        Optional<GroupMember> existingMember = groupMemberRepository.findByGroupChatAndUser(group, joinRequest.getUser());
        if (existingMember.isPresent()) {
            return existingMember.get(); // Return existing member instead of re-adding.
        }

        GroupMember member = new GroupMember();
        member.setGroupChat(group);
        member.setUser(joinRequest.getUser());
        member.setRole(GroupMemberRole.MEMBER);
        member.setJoinedAt(new Date());
        return groupMemberRepository.save(member);
    }


    // For private groups: admin can add a member directly.
    public GroupMember addMemberToPrivateGroup(Long groupId, Long adminId, Long userId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        if (group.getGroupType() != GroupType.PRIVATE) {
            throw new RuntimeException("This method is for private groups only.");
        }
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        GroupMember adminMembership = groupMemberRepository.findByGroupChatAndUser(group, admin)
                .orElseThrow(() -> new RuntimeException("Admin is not a member of the group"));
        if (adminMembership.getRole() != GroupMemberRole.ADMIN) {
            throw new RuntimeException("Only admins can add members in a private group.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Check if the user is already a member.
        if (groupMemberRepository.findByGroupChatAndUser(group, user).isPresent()) {
            throw new RuntimeException("User is already a member of the group.");
        }
        GroupMember member = new GroupMember();
        member.setGroupChat(group);
        member.setUser(user);
        member.setRole(GroupMemberRole.MEMBER);
        member.setJoinedAt(new Date());
        return groupMemberRepository.save(member);
    }

    // Remove a member from the group (by admin) or allow a member to leave.
    @Transactional
    public void removeMember(Long groupId, Long removerId, Long userId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User remover = userRepository.findById(removerId)
                .orElseThrow(() -> new RuntimeException("Remover not found"));
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));
        // If remover is not the same as target, check if remover is an admin.
        if (!remover.getId().equals(target.getId())) {
            GroupMember removerMembership = groupMemberRepository.findByGroupChatAndUser(group, remover)
                    .orElseThrow(() -> new RuntimeException("Remover is not a member of the group"));
            if (removerMembership.getRole() != GroupMemberRole.ADMIN) {
                throw new RuntimeException("Only admins can remove other members.");
            }
        }
        GroupMember membership = groupMemberRepository.findByGroupChatAndUser(group, target)
                .orElseThrow(() -> new RuntimeException("User is not a member of the group"));
        groupMemberRepository.delete(membership);
    }

    // Retrieve all members of a group.
    public List<GroupMember> getGroupMembers(Long groupId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return groupMemberRepository.findByGroupChat(group);
    }

    // (Optional) Promote or demote a member.
    @Transactional
    public GroupMember updateMemberRole(Long groupId, Long adminId, Long userId, GroupMemberRole newRole) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        GroupMember adminMembership = groupMemberRepository.findByGroupChatAndUser(group, admin)
                .orElseThrow(() -> new RuntimeException("Admin is not a member of the group"));
        if (adminMembership.getRole() != GroupMemberRole.ADMIN) {
            throw new RuntimeException("Only admins can update member roles.");
        }
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        GroupMember membership = groupMemberRepository.findByGroupChatAndUser(group, target)
                .orElseThrow(() -> new RuntimeException("User is not a member of the group"));
        membership.setRole(newRole);
        return groupMemberRepository.save(membership);
    }

    // Allows a member to leave a group on their own.
    public GroupChat leaveGroup(Long groupId, Long userId) {
        Optional<GroupChat> groupOpt = groupChatRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        GroupChat group = groupOpt.get();

        // Retrieve the user; throw an exception if the user does not exist.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Prevent group owner from leaving without transferring ownership.
        if (group.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Group owner cannot leave the group. Transfer ownership or delete the group.");
        }

        // Retrieve the membership record for this user and group.
        Optional<GroupMember> membershipOpt = groupMemberRepository.findByGroupChatAndUser(group, user);
        if (membershipOpt.isEmpty()) {
            throw new RuntimeException("User is not a member of this group");
        }
        // Delete the membership record so the user is no longer part of the group.
        groupMemberRepository.delete(membershipOpt.get());
        return group;
    }
}
