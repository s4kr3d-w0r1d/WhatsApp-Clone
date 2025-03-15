package com.whatsappclone.repositories;

import com.whatsappclone.models.GroupChat;
import com.whatsappclone.models.Message;
import com.whatsappclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Retrieve messages between two users (both directions) ordered by timestamp.
    List<Message> findBySenderAndRecipientOrSenderAndRecipientOrderByTimestampAsc(
            User sender1, User recipient1, User sender2, User recipient2);
    List<Message> findByGroupChatOrderByTimestampAsc(GroupChat groupChat);
    List<Message> findByGroupChatId(Long groupId);
    @Query("""
        SELECT m
        FROM Message m
        WHERE LOWER(m.sender.name) LIKE LOWER(CONCAT('%', :username, '%'))
           OR LOWER(m.recipient.name) LIKE LOWER(CONCAT('%', :username, '%'))
        ORDER BY m.timestamp DESC
    """)
    List<Message> findMessagesBySenderOrRecipientName(@Param("username") String username);


}
