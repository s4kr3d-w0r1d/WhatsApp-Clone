package com.whatsappclone.controllers;

import com.whatsappclone.models.UserBlock;
import com.whatsappclone.services.UserBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blocks")
public class UserBlockController {

    private final UserBlockService userBlockService;

    public UserBlockController(UserBlockService userBlockService) {
        this.userBlockService = userBlockService;
    }

    // POST endpoint to block a user.
    @PostMapping("/{blockerId}/{blockedId}")
    public ResponseEntity<UserBlock> blockUser(@PathVariable Long blockerId, @PathVariable Long blockedId) {
        UserBlock userBlock = userBlockService.blockUser(blockerId, blockedId);
        return ResponseEntity.ok(userBlock);
    }

    // DELETE endpoint to unblock a user.
    @DeleteMapping("/{blockerId}/{blockedId}")
    public ResponseEntity<Void> unblockUser(@PathVariable Long blockerId, @PathVariable Long blockedId) {
        userBlockService.unblockUser(blockerId, blockedId);
        return ResponseEntity.ok().build();
    }

    // GET endpoint to retrieve the list of users blocked by the given user.
    @GetMapping("/{blockerId}")
    public ResponseEntity<List<UserBlock>> getBlockedUsers(@PathVariable Long blockerId) {
        List<UserBlock> blockedUsers = userBlockService.getBlockedUsers(blockerId);
        return ResponseEntity.ok(blockedUsers);
    }
}
