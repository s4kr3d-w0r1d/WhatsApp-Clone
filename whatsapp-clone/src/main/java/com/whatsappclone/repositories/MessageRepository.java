package com.whatsappclone.repositories;

import com.whatsappclone.models.Message;
import com.whatsappclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Retrieve messages between two users (both directions) ordered by timestamp.
    List<Message> findBySenderAndRecipientOrSenderAndRecipientOrderByTimestampAsc(
            User sender1, User recipient1, User sender2, User recipient2);
}
