package com.whatsappclone.controllers;

import com.whatsappclone.models.User;
import com.whatsappclone.models.UserProfile;
import com.whatsappclone.services.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    private final UserProfileService profileService;


    public UserProfileController(UserProfileService profileService) {
        this.profileService = profileService;
    }

    // GET endpoint to retrieve a user's profile by userId.
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long userId) {
        UserProfile profile = profileService.getProfileByUserId(userId);
        if (profile.getProfilePictureUrl() != null) {
            String imageUrl = "http://localhost:8080" + profile.getProfilePictureUrl();
            profile.setProfilePictureUrl(imageUrl);
        }
        return ResponseEntity.ok(profile);
    }

    // PUT endpoint to update a user's profile.
    @PutMapping("/{userId}")
    public ResponseEntity<UserProfile> updateProfile(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) MultipartFile profilePicture) {
        UserProfile updatedProfile = profileService.updateProfile(userId, status, bio, profilePicture);
        return ResponseEntity.ok(updatedProfile);
    }
    // Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        List<User> users = profileService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
}

