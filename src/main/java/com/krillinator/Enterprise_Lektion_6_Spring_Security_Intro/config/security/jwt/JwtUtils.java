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
/*
    private final String base64EncodedSecretKey = "your_base64_encoded_secret_key";  // Replace this with your actual base64 encoded secret key
    byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);

    Key key = Keys.hmacShaKeyFor(keyBytes);  // This ensures the key is properly named and sized

    // JWT expiration time (1 hour in milliseconds)
    private final int jwtExpirationMs = (int) TimeUnit.HOURS.toMillis(1);

    public String generateJwtToken(String username) {
        return Jwts.builder()
                .subject(username)  // Set the subject, often the username or user ID
                .issuedAt(new Date())  // Set issued date
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  // Set expiration date
                .signWith(key)  // Use the key created for HMAC
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Log token validation errors (like expiration, malformed, etc.)
            System.err.println("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }

 */
}
