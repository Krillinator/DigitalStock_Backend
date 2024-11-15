package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller.rest;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model.dto.UserRegistrationDTO;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;

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
                        true, true,true,true
                )
        );
        System.out.println(LocalDateTime.now() + " User Was Registered");

        return ResponseEntity.status(HttpStatus.CREATED).body(userRegistrationDTO);
    }

}
