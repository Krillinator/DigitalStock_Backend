package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {
    private final String base64EncodedSecretKey = "U2VjdXJlQXBpX1NlY3JldEtleV9mb3JfSFMyNTYwX3NlY3JldF9wcm9qZWN0X2tleV9leGFtcGxl";  // Replace this with your actual base64 encoded secret key
    byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);

    SecretKey key = Keys.hmacShaKeyFor(keyBytes);  // This ensures the key is properly named and sized

    // JWT expiration time (1 hour in milliseconds)
    private final int jwtExpirationMs = (int) TimeUnit.HOURS.toMillis(1);

    public String generateJwtToken(String username, String role) {
        return Jwts.builder()
                // TODO - Is the username unique?
                .subject(username)  // Set the subject, often the username or user ID
                .claim("role", role)
                .issuedAt(new Date())  // Set issued date
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  // Set expiration date
                .signWith(key)  // Use the key created for HMAC
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public String getRoleFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();


        return claims.get("role", String.class);  // Extract the role from the claims
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()  // Use parserBuilder() instead of deprecated parser()
                    .verifyWith(key)  // Provide the signing key for validation
                    .build()  // Build the JwtParser
                    .parseSignedClaims(authToken);  // Parse and verify the JWT
            return true;  // If no exception is thrown, the token is valid
        } catch (Exception e) {
            // Log token validation errors (like expiration, malformed, etc.)
            System.err.println("Invalid JWT token: " + e.getMessage());
        }
        return false;  // If an exception is thrown, the token is invalid
    }

}
