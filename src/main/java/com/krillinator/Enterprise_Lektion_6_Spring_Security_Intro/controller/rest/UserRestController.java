package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller.rest;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model.dto.UserRegistrationDTO;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

// /api/v1
@RestController
public class UserRestController extends AbstractApiRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {

        System.out.println("DEBUGGING REST_CONTROLLER");
        System.out.println("User Being Registered");
        System.out.println("DEBUGGING REST_CONTROLLER");

        return ResponseEntity.ok().body("Hello");
    }

    @GetMapping("/who-am-i")
    public ResponseEntity<String> checkedLoggedInUser(HttpServletRequest request) {

        System.out.println("Headers received:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
                System.out.println(headerName + ": " + request.getHeader(headerName))
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("---who-am-i---");
        System.out.println(authentication);

        // Check if the user is authenticated (it won't be null if the filter worked correctly)
        if (authentication != null && authentication.isAuthenticated()) {
            // Extract the username (or any other user details from the authentication object)
            System.out.println(authentication.getAuthorities());
            String username = authentication.getName();  // The username is typically stored as the principal
            System.out.println(username);
            return ResponseEntity.ok("Logged in user: " + username + authentication.getAuthorities());
        } else {
            return ResponseEntity.status(401).body("User is not authenticated");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationDTO> registerUser(
            @Valid @RequestBody UserRegistrationDTO userRegistrationDTO,
            BindingResult bindingResult
    ) {

        System.out.println(userRegistrationDTO);

        if (bindingResult.hasErrors()) {
            ResponseEntity.badRequest().build();
        }

        userRepository.save(
                new CustomUser(
                        userRegistrationDTO.username(),
                        passwordEncoder.encode(userRegistrationDTO.password()),
                        UserRole.ADMIN,
                        true, true, true, true
                )
        );
        System.out.println(LocalDateTime.now() + " User Was Registered");

        return ResponseEntity.status(HttpStatus.CREATED).body(userRegistrationDTO);
    }

}
