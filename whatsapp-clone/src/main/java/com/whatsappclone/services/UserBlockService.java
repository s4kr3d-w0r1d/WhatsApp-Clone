package com.whatsappclone.services;

import com.whatsappclone.models.User;
import com.whatsappclone.models.UserBlock;
import com.whatsappclone.repositories.UserBlockRepository;
import com.whatsappclone.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;
    private final UserRepository userRepository;

    public UserBlockService(UserBlockRepository userBlockRepository, UserRepository userRepository) {
        this.userBlockRepository = userBlockRepository;
        this.userRepository = userRepository;
    }

    // Block a user: the blocker blocks the other user.
    public UserBlock blockUser(Long blockerId, Long blockedId) {
        Optional<User> blockerOpt = userRepository.findById(blockerId);
        Optional<User> blockedOpt = userRepository.findById(blockedId);

        if (blockerOpt.isEmpty() || blockedOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User blocker = blockerOpt.get();
        User blocked = blockedOpt.get();

        // Check if a block already exists.
        if (userBlockRepository.findByBlockerAndBlocked(blocker, blocked).isPresent()) {
            throw new RuntimeException("User is already blocked");
        }
        UserBlock userBlock = new UserBlock();
        userBlock.setBlocker(blocker);
        userBlock.setBlocked(blocked);
        return userBlockRepository.save(userBlock);
    }

    // Unblock a user.
    public void unblockUser(Long blockerId, Long blockedId) {
        Optional<User> blockerOpt = userRepository.findById(blockerId);
        Optional<User> blockedOpt = userRepository.findById(blockedId);
        if (blockerOpt.isEmpty() || blockedOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User blocker = blockerOpt.get();
        User blocked = blockedOpt.get();
        UserBlock block = userBlockRepository.findByBlockerAndBlocked(blocker, blocked)
                .orElseThrow(() -> new RuntimeException("Block not found"));
        userBlockRepository.delete(block);
    }

    // Get all users that the blocker has blocked.
    public List<UserBlock> getBlockedUsers(Long blockerId) {
        Optional<User> blockerOpt = userRepository.findById(blockerId);
        if (blockerOpt.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return userBlockRepository.findByBlocker(blockerOpt.get());
    }

    // Returns true if either user has blocked the other.

}
