package com.whatsappclone.services;

import com.whatsappclone.models.Message;
import com.whatsappclone.models.User;
import com.whatsappclone.repositories.MessageRepository;
import com.whatsappclone.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // Send a message from sender to recipient with given content.
    public Message sendMessage(Long senderId, Long recipientId, String content) {
        Optional<User> senderOpt = userRepository.findById(senderId);
        Optional<User> recipientOpt = userRepository.findById(recipientId);
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            throw new RuntimeException("Sender or recipient not found");
        }
        Message message = new Message();
        message.setSender(senderOpt.get());
        message.setRecipient(recipientOpt.get());
        message.setContent(content);
        message.setTimestamp(new Date());
        return messageRepository.save(message);
    }

    // Retrieve the full conversation between two users (both directions), ordered by timestamp.
    public List<Message> getChatHistory(Long userId1, Long userId2) {
        Optional<User> user1Opt = userRepository.findById(userId1);
        Optional<User> user2Opt = userRepository.findById(userId2);
        if (user1Opt.isEmpty() || user2Opt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user1 = user1Opt.get();
        User user2 = user2Opt.get();
        return messageRepository.findBySenderAndRecipientOrSenderAndRecipientOrderByTimestampAsc(
                user1, user2, user2, user1);
    }
}
