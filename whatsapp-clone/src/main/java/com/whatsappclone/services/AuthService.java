package com.whatsappclone.services;

import com.whatsappclone.models.User;
import com.whatsappclone.models.UserKeys;
import com.whatsappclone.repositories.UserKeysRepository;
import com.whatsappclone.repositories.UserRepository;
import com.whatsappclone.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final UserKeysRepository userKeysRepository;
    private final Set<String> tokenBlacklist = new HashSet<>();

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil, UserKeysRepository userKeysRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.userKeysRepository = userKeysRepository;
    }

    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        // Create new user
        User user = new User(null, email, bCryptPasswordEncoder.encode(password), username);
        userRepository.save(user);

        // Generate encryption keys using Diffie–Hellman (DH)
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

            UserKeys userKeys = new UserKeys();
            userKeys.setUserId(user.getId());
            userKeys.setPublicKeyBytes(publicKeyBytes);
            userKeys.setPrivateKeyBytes(privateKeyBytes);

            userKeysRepository.save(userKeys);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate encryption keys", e);
        }

        return jwtUtil.generateToken(username);
    }
    public Map<String, Object> loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || !bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }
        User userDetails = user.get();
        userDetails.setOnline(true);
        userRepository.save(userDetails);
        String token = jwtUtil.generateToken(email);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user.get());

        return response;
}
    public String logoutUser(String token) {
        String email = jwtUtil.extractUsername(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return "User not found";
        }
        User user = userOptional.get();
        if (!user.isOnline()) {
            return "User already logged out";
        }
        user.setOnline(false);
        userRepository.save(user);
        jwtUtil.blacklistToken(token);
        return "Logout successful";
    }
}