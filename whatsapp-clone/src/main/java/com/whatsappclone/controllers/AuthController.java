package com.whatsappclone.controllers;

import com.whatsappclone.dto.LoginRequest;
import com.whatsappclone.dto.RegisterRequest;
import com.whatsappclone.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class  AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String token = authService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginRequest request) {
        Map<String,Object> response = authService.loginUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}