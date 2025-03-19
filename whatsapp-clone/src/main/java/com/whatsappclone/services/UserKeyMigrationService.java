package com.whatsappclone.services;

import com.whatsappclone.models.User;
import com.whatsappclone.models.UserKeys;
import com.whatsappclone.repositories.UserKeysRepository;
import com.whatsappclone.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserKeyMigrationService {

    private final UserRepository userRepository;
    private final UserKeysRepository userKeysRepository;
    private final KeyPairGenerator keyPairGenerator;

    public UserKeyMigrationService(UserRepository userRepository, UserKeysRepository userKeysRepository) {
        this.userRepository = userRepository;
        this.userKeysRepository = userKeysRepository;
        try {
            this.keyPairGenerator = KeyPairGenerator.getInstance("DH");
            this.keyPairGenerator.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize key pair generator", e);
        }
    }

    @PostConstruct
    public void migrateKeysForAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!userKeysRepository.existsByUserId(user.getId())) {
                try {
                    KeyPair keyPair = keyPairGenerator.generateKeyPair();
                    UserKeys userKey = new UserKeys();
                    userKey.setUserId(user.getId());
                    userKey.setPublicKeyBytes(keyPair.getPublic().getEncoded());
                    userKey.setPrivateKeyBytes(keyPair.getPrivate().getEncoded());
                    userKeysRepository.save(userKey);
                    System.out.println("Generated keys for user: " + user.getName());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to generate keys for user: " + user.getName(), e);
                }
            }
        }
    }
}
