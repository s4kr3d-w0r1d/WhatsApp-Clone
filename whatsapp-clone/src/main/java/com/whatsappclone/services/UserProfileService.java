package com.whatsappclone.services;

import com.whatsappclone.models.User;
import com.whatsappclone.models.UserProfile;
import com.whatsappclone.repositories.UserProfileRepository;
import com.whatsappclone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository profileRepository;
    private final UserRepository userRepository;

    // Inject the uploads path from properties, default to "C:/uploads" if not set.
    @Value("${uploads.path:C:/uploads}")
    private String uploadPath;

    public UserProfileService(UserProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    // Retrieve profile by user ID.
    public UserProfile getProfileByUserId(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return profileRepository.findByUser(userOpt.get())
                .orElse(new UserProfile(null, userOpt.get(), "", "", null));
    }

    // Update profile with status, bio, and optionally a profile picture.
    public UserProfile updateProfile(Long userId, String status, String bio, MultipartFile profilePicture) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        // Retrieve existing profile or create new one if not present.
        UserProfile profile = profileRepository.findByUser(user).orElse(new UserProfile());
        profile.setUser(user);
        profile.setStatus(status);
        profile.setBio(bio);

        // Handle profile picture upload if provided.
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + profilePicture.getOriginalFilename();
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                // Create the directory and any necessary parent directories.
                if (!uploadDir.mkdirs()) {
                    throw new RuntimeException("Failed to create directory for profile pictures");
                }
            }
            File dest = new File(uploadDir, fileName);
            try {
                profilePicture.transferTo(dest);
                // Store the file path or a URL relative to your server.
                profile.setProfilePictureUrl("/uploads/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save profile picture", e);
            }
        }
        return profileRepository.save(profile);
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }
}
