package com.whatsappclone.controllers;

import com.whatsappclone.models.User;
import com.whatsappclone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserStatusController {

    private final UserRepository userRepository;

    @Autowired
    public UserStatusController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET endpoint to retrieve the online status of a user by id.
    @GetMapping("/{userId}/status")
    public ResponseEntity<Boolean> getUserStatus(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get().isOnline());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
