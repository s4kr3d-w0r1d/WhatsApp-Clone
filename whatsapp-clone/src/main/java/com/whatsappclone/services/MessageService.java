package com.whatsappclone.services;

import com.whatsappclone.models.Message;
import com.whatsappclone.models.User;
import com.whatsappclone.repositories.MessageRepository;
import com.whatsappclone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    // Define the upload directory for chat media. (Set in application.properties)
    @Value("${uploads.chat:C:/uploads/chat}")
    private String chatUploadPath;

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

    public Message sendMediaMessage(Long senderId, Long recipientId, String content, MultipartFile mediaFile, String mediaType) {
        Optional<User> senderOpt = userRepository.findById(senderId);
        Optional<User> recipientOpt = userRepository.findById(recipientId);
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            throw new RuntimeException("Sender or recipient not found");
        }
        Message message = new Message();
        message.setSender(senderOpt.get());
        message.setRecipient(recipientOpt.get());
        message.setContent(content);  // Text is optional; can be empty
        message.setTimestamp(new Date());

        if (mediaFile != null && !mediaFile.isEmpty()) {
            // Generate a unique filename
            String fileName = System.currentTimeMillis() + "_" + mediaFile.getOriginalFilename();
            File uploadDir = new File(chatUploadPath);
            if (!uploadDir.exists()) {
                if (!uploadDir.mkdirs()) {
                    throw new RuntimeException("Failed to create directory for chat media");
                }
            }
            File dest = new File(uploadDir, fileName);
            try {
                mediaFile.transferTo(dest);
                // Store a URL or path; adjust this based on how you serve static files.
                message.setMediaUrl("/uploads/chat/" + fileName);
                message.setMediaType(mediaType);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save media file", e);
            }
        }
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
