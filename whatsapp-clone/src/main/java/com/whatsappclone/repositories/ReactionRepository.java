package com.whatsappclone.repositories;

import com.whatsappclone.models.Message;
import com.whatsappclone.models.Reaction;
import com.whatsappclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    // Find all reactions for a given message
    List<Reaction> findByMessage(Message message);

    // Find a reaction by message and user (to check if a reaction already exists)
    Optional<Reaction> findByMessageAndUser(Message message, User user);
}
