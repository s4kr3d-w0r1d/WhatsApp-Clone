package com.whatsappclone.repositories;

import com.whatsappclone.models.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
    // No need to define findById, it's already inherited from JpaRepository.
}
