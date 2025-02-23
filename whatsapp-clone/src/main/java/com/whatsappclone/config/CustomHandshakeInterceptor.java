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
        // Extract "user" from query parameters (for testing purposes)
        String user = request.getURI().getQuery(); // For simplicity; you may want to parse the query string properly.
        if (user != null && user.startsWith("user=")) {
            attributes.put("user", user.substring(5)); // Extract the value after "user="
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
