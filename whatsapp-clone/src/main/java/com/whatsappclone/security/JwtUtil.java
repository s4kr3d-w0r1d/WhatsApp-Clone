package com.whatsappclone.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret:}")
    private String secretKey; // May be empty if not set in properties

    // Hard-coded expiration time: 24 hours in milliseconds
    private static final long EXPIRATION_TIME = 86400000L;

    private Key key;

    @PostConstruct
    public void init() {
        // If no secretKey is provided or it's too short, generate a secure one.
        if (secretKey == null || secretKey.trim().isEmpty() || Decoders.BASE64.decode(secretKey).length < 32) {
            Key generatedKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            secretKey = Encoders.BASE64.encode(generatedKey.getEncoded());
            // For production, you should log a warning and persist the key instead of generating it every time.
            System.out.println("Generated fallback JWT secret: " + secretKey);
        }
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
