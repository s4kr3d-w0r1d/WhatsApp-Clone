//package com.whatsappclone.config;
//
//import com.whatsappclone.security.JwtUtil;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Value("${cors.allowedOrigins}")
//    private String[] allowedOrigins;
//
//    private final JwtUtil jwtUtil;
//
//    public WebSocketConfig(JwtUtil jwtUtil) {  // Inject JwtUtil via constructor
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns(allowedOrigins)
//                .setHandshakeHandler(new CustomPrincipalHandshakeHandler(jwtUtil)) // Pass JwtUtil
//                .withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic", "/queue");
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.setUserDestinationPrefix("/user");
//    }
//}

package com.whatsappclone.config;

import com.whatsappclone.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${cors.allowedOrigins}")
    private String[] allowedOrigins;

    private final JwtUtil jwtUtil;

    public WebSocketConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(allowedOrigins)
                .setHandshakeHandler(new CustomPrincipalHandshakeHandler(jwtUtil)) // Ensure correct JWT extraction
                .withSockJS(); // If causing issues, remove .withSockJS()
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Broker to send messages
        registry.setApplicationDestinationPrefixes("/app"); // Messages sent to "/app/**"
        registry.setUserDestinationPrefix("/user"); // Private messaging
    }
}

