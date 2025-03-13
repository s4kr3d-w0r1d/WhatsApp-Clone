package com.whatsappclone.repositories;

import com.whatsappclone.models.GroupChat;
import com.whatsappclone.models.GroupMember;
import com.whatsappclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroupChatAndUser(GroupChat groupChat, User user);
    List<GroupMember> findByGroupChat(GroupChat groupChat);
    boolean existsByGroupChatAndUser(GroupChat groupChat, User user);
}
