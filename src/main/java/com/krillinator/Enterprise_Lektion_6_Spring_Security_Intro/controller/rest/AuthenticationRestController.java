package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller.rest;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config.security.CustomUserDetails;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config.security.jwt.JwtUtils;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model.dto.UserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public String authenticateUser(@RequestParam String username, @RequestParam String password) {
        // Authenticate user using AuthenticationManager
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);  // This triggers the CustomUserDetailsService

        // IMPORTANT - MUST BE THE SAME TYPE AS RETURNED WITHIN - CUSTOM_USER_DETAILS_SERVICE CLASS
        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();

        // Type-check
        if (customUser instanceof CustomUserDetails customUserDetails) {

            return jwtUtils.generateJwtToken(customUserDetails.getUsername(), "ADMIN");
        } else {
            throw new IllegalStateException("Authenticated principal is not of type CustomUserDetails.");
        }

    }


}
