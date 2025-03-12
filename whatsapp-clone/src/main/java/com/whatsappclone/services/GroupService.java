package com.whatsappclone.services;

import com.whatsappclone.dto.GroupMemberDTO;
import com.whatsappclone.models.GroupChat;
import com.whatsappclone.models.GroupMember;
import com.whatsappclone.models.User;
import com.whatsappclone.repositories.GroupChatRepository;
import com.whatsappclone.repositories.GroupMemberRepository;
import com.whatsappclone.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupChatRepository groupChatRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public GroupService(GroupChatRepository groupChatRepository, GroupMemberRepository groupMemberRepository, UserRepository userRepository) {
        this.groupChatRepository = groupChatRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    // Create a new group; the creator becomes the owner and first member.
    public GroupChat createGroup(Long ownerId, String groupName, String description) {
        Optional<User> ownerOpt = userRepository.findById(ownerId);
        if (ownerOpt.isEmpty()) {
            throw new RuntimeException("Owner not found");
        }
        User owner = ownerOpt.get();
        GroupChat group = new GroupChat();
        group.setName(groupName);
        group.setDescription(description);
        group.setOwner(owner);
        group.setCreatedAt(new Date());
        GroupChat savedGroup = groupChatRepository.save(group);

        // Automatically add the owner as a member.
        GroupMember member = new GroupMember();
        member.setGroupChat(savedGroup);
        member.setUser(owner);
        groupMemberRepository.save(member);
        return savedGroup;
    }

    // Add a user to an existing group (only the owner can add).
    public GroupChat addMember(Long groupId, Long ownerId, Long userId) {
        Optional<GroupChat> groupOpt = groupChatRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        GroupChat group = groupOpt.get();
        // Ensure the ownerId matches the group's owner.
        if (!group.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Only the group owner can add members");
        }
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        // Check if the user is already a member.
        Optional<GroupMember> existingMember = groupMemberRepository.findByGroupChatAndUser(group, userOpt.get());
        if (existingMember.isPresent()) {
            throw new RuntimeException("User is already a member of this group");
        }
        GroupMember member = new GroupMember();
        member.setGroupChat(group);
        member.setUser(userOpt.get());
        groupMemberRepository.save(member);
        return group;
    }

    // Remove a user from the group (only the owner can remove).
    public GroupChat removeMember(Long groupId, Long ownerId, Long userId) {
        Optional<GroupChat> groupOpt = groupChatRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        GroupChat group = groupOpt.get();
        if (!group.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Only the group owner can remove members");
        }
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        GroupMember member = groupMemberRepository.findByGroupChatAndUser(group, userOpt.get())
                .orElseThrow(() -> new RuntimeException("User is not a member of this group"));
        groupMemberRepository.delete(member);
        return group;
    }

    // Retrieve all members of a group as DTOs.
    public List<GroupMemberDTO> getGroupMembers(Long groupId) {
        Optional<GroupChat> groupOpt = groupChatRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        List<GroupMember> members = groupMemberRepository.findByGroupChat(groupOpt.get());
        return members.stream()
                .map(member -> new GroupMemberDTO(
                        member.getId(),
                        member.getGroupChat().getId(),
                        member.getGroupChat().getName(),
                        member.getUser().getId(),
                        member.getUser().getName()))
                .collect(Collectors.toList());
    }
}
