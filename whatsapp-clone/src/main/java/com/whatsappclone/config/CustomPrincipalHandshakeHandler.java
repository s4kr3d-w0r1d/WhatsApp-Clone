//package com.whatsappclone.config;
//
//import com.whatsappclone.security.JwtUtil;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//
//import java.security.Principal;
//import java.util.Map;
//
//public class CustomPrincipalHandshakeHandler extends DefaultHandshakeHandler {
//
//    private final JwtUtil jwtUtil;
//
//    public CustomPrincipalHandshakeHandler(JwtUtil jwtUtil) {  // Constructor to accept JwtUtil
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//        String query = request.getURI().getQuery();
//        if (query != null && query.startsWith("token=")) {
//            String token = query.substring(6);
//            String username = jwtUtil.extractUsername(token);
//            return () -> username; // Return authenticated user as Principal
//        }
//        return () -> "anonymousUser";
//    }
//}

package com.whatsappclone.config;

import com.whatsappclone.security.JwtUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomPrincipalHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtUtil jwtUtil;

    public CustomPrincipalHandshakeHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = extractTokenFromQuery(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return () -> "anonymousUser"; // If invalid, default to anonymous
        }

        String username = jwtUtil.extractUsername(token);
        return username != null ? () -> username : () -> "anonymousUser";
    }

    /**
     * Extracts the token from the WebSocket query parameters.
     */
    private String extractTokenFromQuery(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("token=")) {
                    return param.substring(6); // Extract token value
                }
            }
        }
        return null; // No token found
    }
}

