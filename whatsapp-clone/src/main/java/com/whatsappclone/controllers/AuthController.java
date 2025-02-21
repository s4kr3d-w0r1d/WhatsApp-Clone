package com.whatsappclone.controllers;

import com.whatsappclone.services.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody Map<String, String> request) {
        return authService.registerUser(request.get("name"), request.get("email"), request.get("password"));
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Map<String, String> request) {
        return authService.loginUser(request.get("email"), request.get("password"));
    }
}
