package com.whatsappclone.listeners;

import com.whatsappclone.models.User;
import com.whatsappclone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;
import java.util.Optional;

@Component
public class WebSocketConnectListener implements ApplicationListener<SessionConnectedEvent> {

    private final UserRepository userRepository;

    @Autowired
    public WebSocketConnectListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = headerAccessor.getUser();
        if (principal != null) {
            // Assume principal.getName() returns the user’s email or username.
            Optional<User> userOpt = userRepository.findByEmail(principal.getName());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setOnline(true);
                userRepository.save(user);
                System.out.println("User " + user.getEmail() + " marked as online.");
            }
        }
    }
}
