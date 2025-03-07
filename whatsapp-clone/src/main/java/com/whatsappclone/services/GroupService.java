package com.whatsappclone.services;

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

    // Add a user to an existing group.
    public GroupMember addMember(Long groupId, Long userId) {
        Optional<GroupChat> groupOpt = groupChatRepository.findById(groupId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (groupOpt.isEmpty() || userOpt.isEmpty()) {
            throw new RuntimeException("Group or user not found");
        }
        // Check if already a member
        if (groupMemberRepository.findByGroupChatAndUser(groupOpt.get(), userOpt.get()).isPresent()) {
            throw new RuntimeException("User is already a member of this group");
        }
        GroupMember member = new GroupMember();
        member.setGroupChat(groupOpt.get());
        member.setUser(userOpt.get());
        return groupMemberRepository.save(member);
    }

    // Remove a member from the group.
    public GroupChat removeMember(Long groupId, Long userId) {
        Optional<GroupChat> groupOpt = groupChatRepository.findById(groupId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (groupOpt.isEmpty() || userOpt.isEmpty()) {
            throw new RuntimeException("Group or user not found");
        }
        GroupMember member = groupMemberRepository.findByGroupChatAndUser(groupOpt.get(), userOpt.get())
                .orElseThrow(() -> new RuntimeException("User is not a member of this group"));
        groupMemberRepository.delete(member);
        return groupOpt.get();
    }

    // Retrieve all members of a group.
    public List<GroupMember> getGroupMembers(Long groupId) {
        Optional<GroupChat> groupOpt = groupChatRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        return groupMemberRepository.findByGroupChat(groupOpt.get());
    }
}
