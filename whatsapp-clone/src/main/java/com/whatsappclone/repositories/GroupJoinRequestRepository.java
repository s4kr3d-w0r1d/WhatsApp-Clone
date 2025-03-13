package com.whatsappclone.repositories;

import com.whatsappclone.models.GroupChat;
import com.whatsappclone.models.GroupJoinRequest;
import com.whatsappclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupJoinRequestRepository extends JpaRepository<GroupJoinRequest, Long> {
    Optional<GroupJoinRequest> findByGroupChatAndUser(GroupChat groupChat, User user);
    List<GroupJoinRequest> findByGroupChatAndStatus(GroupChat groupChat, com.whatsappclone.models.RequestStatus status);
}
