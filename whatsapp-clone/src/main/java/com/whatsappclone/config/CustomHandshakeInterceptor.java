package com.whatsappclone.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery(); // Full query string (e.g., "user=John")
        if (query != null && query.contains("user=")) {
            String username = query.split("user=")[1].split("&")[0]; // Extract username safely
            attributes.put("user", username);
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
