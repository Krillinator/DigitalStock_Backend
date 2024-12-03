package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller.rest;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.config.security.CustomUserDetails;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.config.security.jwt.JwtUtils;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.dto.UserRegistrationDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v2/auth")
public class AuthenticationRestController {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationRestController(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/test")
    public String test() {

        return "Hello";
    }

    @PostMapping("/body-login")
    public String authenticateUser(@RequestBody UserRegistrationDTO user) {

        if (user != null) {
            return "Yes";
        }

        return "HMMM";
    }

    @PostMapping("/post-test")
    public ResponseEntity<String> authenticateUser() {

        return ResponseEntity.ok("Hello from /post-test in text form (NOT JSON)");
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        try {
            // Authenticate user using AuthenticationManager
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);  // This triggers the CustomUserDetailsService

            // IMPORTANT - MUST BE THE SAME TYPE AS RETURNED WITHIN - CUSTOM_USER_DETAILS_SERVICE CLASS
            CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();

            // Type-check
            if (customUser instanceof CustomUserDetails customUserDetails) {
                System.out.println("-------------------");
                System.out.println(customUserDetails.getAuthorities().toString());
                System.out.println("-------------------");
                final String token = jwtUtils.generateJwtToken(
                        customUserDetails.getUsername(),
                        customUserDetails.getAuthorities().toString()
                );

                // Prepare Cookie
                Cookie cookie = new Cookie("authToken", token);
                cookie.setHttpOnly(true); // No JS (prevent XSS)
                cookie.setSecure(false); // HTTPS
                cookie.setPath("/"); // Available to whole App
                cookie.setMaxAge((int) TimeUnit.HOURS.toSeconds(1));
                response.addCookie(cookie);

                System.out.println(cookie);

                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.internalServerError().body("Authenticated principal is not of type CustomUserDetails");
            }
        } catch (BadCredentialsException e) {

            // Important - This can't be caught by global exception handler - handle manually
            return ResponseEntity.status(401).body("Bad Credentials..");
        }

    }


}
