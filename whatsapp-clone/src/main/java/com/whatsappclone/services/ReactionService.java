package com.whatsappclone.services;

import com.whatsappclone.models.Message;
import com.whatsappclone.models.Reaction;
import com.whatsappclone.models.User;
import com.whatsappclone.repositories.MessageRepository;
import com.whatsappclone.repositories.ReactionRepository;
import com.whatsappclone.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ReactionService(ReactionRepository reactionRepository,
                           MessageRepository messageRepository,
                           UserRepository userRepository) {
        this.reactionRepository = reactionRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    /**
     * Adds or updates a reaction from a user on a message.
     * If the user already reacted to the message, update the reaction type.
     * Otherwise, create a new reaction.
     */
    public Reaction addReaction(Long messageId, Long userId, String reactionType) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Reaction> existingReaction = reactionRepository.findByMessageAndUser(message, user);
        if (existingReaction.isPresent()) {
            Reaction reaction = existingReaction.get();
            reaction.setType(reactionType);
            return reactionRepository.save(reaction);
        } else {
            Reaction reaction = new Reaction();
            reaction.setMessage(message);
            reaction.setUser(user);
            reaction.setType(reactionType);
            return reactionRepository.save(reaction);
        }
    }

    /**
     * Retrieves all reactions for a given message.
     */
    public List<Reaction> getReactionsForMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        return reactionRepository.findByMessage(message);
    }

    /**
     * Removes a reaction for a given message by a specific user.
     */
    public void removeReaction(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Reaction reaction = reactionRepository.findByMessageAndUser(message, user)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));
        reactionRepository.delete(reaction);
    }
}
