package com.whatsappclone.services;

import com.whatsappclone.dto.ChatSearchResponse;
import com.whatsappclone.models.GroupChat;
import com.whatsappclone.models.Message;
import com.whatsappclone.models.User;
import com.whatsappclone.models.UserBlock;
import com.whatsappclone.models.UserKeys;
import com.whatsappclone.repositories.GroupChatRepository;
import com.whatsappclone.repositories.GroupMemberRepository;
import com.whatsappclone.repositories.MessageRepository;
import com.whatsappclone.repositories.UserBlockRepository;
import com.whatsappclone.repositories.UserKeysRepository;
import com.whatsappclone.repositories.UserRepository;
import com.whatsappclone.util.E2EECryptoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final GroupChatRepository groupChatRepository;
    private final UserBlockRepository blockRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserKeysRepository userKeysRepository;

    // Directory where media files will be saved (configure in application.properties)
    @Value("${uploads.chat:C:/uploads/chat}")
    private String chatUploadPath;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository,
                          GroupChatRepository groupChatRepository, UserBlockRepository blockRepository,
                          GroupMemberRepository groupMemberRepository, UserKeysRepository userKeysRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.groupChatRepository = groupChatRepository;
        this.blockRepository = blockRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userKeysRepository = userKeysRepository;
    }

    // Sends a one-to-one text message.
    public Message sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Check if sender is blocked by recipient
        Optional<UserBlock> block = blockRepository.findByBlockerAndBlocked(sender, recipient);
        if (block.isPresent()) {
            throw new RuntimeException("You are blocked by the recipient and cannot send messages.");
        }

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setTimestamp(new Date());
        return messageRepository.save(message);
    }

    // Sends a one-to-one media message.
    public Message sendMediaMessage(Long senderId, Long recipientId, String content,
                                    MultipartFile mediaFile, String mediaType) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Check if sender is blocked by recipient
        Optional<UserBlock> block = blockRepository.findByBlockerAndBlocked(sender, recipient);
        if (block.isPresent()) {
            throw new RuntimeException("You are blocked by the recipient and cannot send messages.");
        }

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setTimestamp(new Date());

        if (mediaFile != null && !mediaFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + mediaFile.getOriginalFilename();
            File uploadDir = new File(chatUploadPath);
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new RuntimeException("Failed to create directory for chat media");
            }
            File dest = new File(uploadDir, fileName);
            try {
                mediaFile.transferTo(dest);
                message.setMediaUrl("/uploads/chat/" + fileName);
                message.setMediaType(mediaType);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save media file", e);
            }
        }
        return messageRepository.save(message);
    }

    // Helper method to upload media and return its URL.
    public String uploadMedia(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File uploadDir = new File(chatUploadPath);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new RuntimeException("Failed to create directory for chat media");
        }
        File dest = new File(uploadDir, fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save media file", e);
        }
        return "/uploads/chat/" + fileName;
    }

    // Retrieves one-to-one chat history.
    public List<Message> getChatHistory(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return messageRepository.findBySenderAndRecipientOrSenderAndRecipientOrderByTimestampAsc(
                user1, user2, user2, user1);
    }

    // Retrieves the group chat history ordered by timestamp.
    public List<Message> getGroupChatHistory(Long groupId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return messageRepository.findByGroupChatOrderByTimestampAsc(group);
    }

    // Sends a group message. Only members (or admins) of the group can send messages.
    public Message sendGroupMessage(Long senderId, Long groupId, String content, String mediaUrl, String mediaType) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Check if sender is a member of the group
        if (!groupMemberRepository.existsByGroupChatAndUser(group, sender)) {
            throw new RuntimeException("User is not a member of the group");
        }

        Message message = new Message();
        message.setSender(sender);
        // Force lazy-loading of group owner (for JSON serialization)
        group.getOwner().getEmail();
        message.setGroupChat(group);
        message.setContent(content);
        message.setTimestamp(new Date());

        if (mediaUrl != null && !mediaUrl.trim().isEmpty() && mediaType != null && !mediaType.trim().isEmpty()) {
            message.setMediaUrl(mediaUrl);
            message.setMediaType(mediaType);
        }
        return messageRepository.save(message);
    }

    // Mark a message as delivered.
    public Message markMessageAsDelivered(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setDeliveredAt(new Date());
        return messageRepository.save(message);
    }

    // Mark a message as read.
    public Message markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setReadAt(new Date());
        return messageRepository.save(message);
    }

    // Search messages by sender or recipient name.
    public List<Message> searchMessagesByUsername(String username) {
        return messageRepository.findMessagesBySenderOrRecipientName(username);
    }

    // Search chats and profile for a given user.
    public ChatSearchResponse searchChatsAndProfile(Long searcherId, String searchedName) {
        List<User> searchedUsers = userRepository.findByNameContainingIgnoreCase(searchedName);
        if (searchedUsers.isEmpty()) {
            throw new RuntimeException("No user found with name containing: " + searchedName);
        }
        User searchedUser = searchedUsers.get(0);
        User searcher = userRepository.findById(searcherId)
                .orElseThrow(() -> new RuntimeException("Searcher not found"));
        List<Message> messages = messageRepository.findBySenderAndRecipientOrSenderAndRecipientOrderByTimestampAsc(
                searcher, searchedUser, searchedUser, searcher);
        return new ChatSearchResponse(searchedUser, messages);
    }

    /**
     * Example: Send an end-to-end encrypted text message from sender to recipient.
     */
    public Message sendEncryptedMessage(Long senderId, Long recipientId, String plaintext) throws Exception {
        // 1. Load sender and recipient
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        // 2. Load their DH key records
        UserKeys senderKeys = userKeysRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender keys not found"));
        UserKeys recipientKeys = userKeysRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient keys not found"));

        // 3. Construct KeyFactory for DH
        KeyFactory keyFactory = KeyFactory.getInstance("DH");

        // 4. Convert stored bytes into keys
        PrivateKey senderPrivateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(senderKeys.getPrivateKeyBytes()));
        PublicKey recipientPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(recipientKeys.getPublicKeyBytes()));

        // 5. Compute shared secret using DH
        byte[] sharedSecret = E2EECryptoUtil.computeSharedSecret(senderPrivateKey, recipientPublicKey);

        // 6. Derive an AES key from the shared secret
        SecretKeySpec aesKey = E2EECryptoUtil.deriveAESKey(sharedSecret);

        // 7. Encrypt the plaintext
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] cipherBytes = E2EECryptoUtil.encryptAES(plaintextBytes, aesKey);

        // 8. Create and store the message.
        // Set "content" to an empty string so the DB NOT NULL constraint is satisfied.
        Message msg = new Message();
        msg.setSender(sender);
        msg.setRecipient(recipient);
        msg.setContent(""); // satisfy the not-null constraint for content
        msg.setEncryptedContent(cipherBytes);
        msg.setTimestamp(new Date());

        return messageRepository.save(msg);
    }

    /**
     * Decrypt a message for the viewer.
     */
    public String decryptMessage(Long messageId, Long viewerId) throws Exception {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Determine the other user's ID
        Long otherUserId;
        if (msg.getSender().getId().equals(viewerId)) {
            otherUserId = msg.getRecipient().getId();
        } else if (msg.getRecipient().getId().equals(viewerId)) {
            otherUserId = msg.getSender().getId();
        } else {
            throw new RuntimeException("Viewer is neither sender nor recipient");
        }

        // Load keys for viewer and the other user
        UserKeys viewerKeys = userKeysRepository.findById(viewerId)
                .orElseThrow(() -> new RuntimeException("Viewer keys not found"));
        UserKeys otherKeys = userKeysRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("Other user's keys not found"));

        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        PrivateKey viewerPrivateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(viewerKeys.getPrivateKeyBytes()));
        PublicKey otherPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(otherKeys.getPublicKeyBytes()));

        // Compute shared secret from viewer's perspective
        byte[] sharedSecret = E2EECryptoUtil.computeSharedSecret(viewerPrivateKey, otherPublicKey);
        SecretKeySpec aesKey = E2EECryptoUtil.deriveAESKey(sharedSecret);

        // Decrypt the encrypted content
        byte[] plaintextBytes = E2EECryptoUtil.decryptAES(msg.getEncryptedContent(), aesKey);
        return new String(plaintextBytes, StandardCharsets.UTF_8);
    }
}
