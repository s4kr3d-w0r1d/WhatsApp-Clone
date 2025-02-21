package com.whatsappclone.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secretKey;  // Secret key (should be Base64-encoded) from application.properties

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;  // Expiration time in milliseconds

    // Generates a JWT token for the given username.
    public String generateToken(String username) {
        // Decode the Base64 secret key into a byte array.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Generate a cryptographic key suitable for HMAC-SHA algorithms.
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(username)  // Set the 'sub' (subject) claim to the username.
                .setIssuedAt(new Date())  // Set the token issuance time.
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))  // Set token expiration.
                .signWith(key, SignatureAlgorithm.HS256)  // Sign the token with our key and the HS256 algorithm.
                .compact();  // Build and serialize the token as a compact URL-safe string.
    }

    // Extracts the username (subject) from the given JWT token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extracts the expiration date from the token.
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract a specific claim from the token using a claim's resolver.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Rebuild the key for parsing the token.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        // Build a JWT parser with the signing key.
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        // Use the provided function to extract the desired claim.
        return claimsResolver.apply(claims);
    }

    // Validates the token by checking that the username matches and the token is not expired.
    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Returns true if the token is expired.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
