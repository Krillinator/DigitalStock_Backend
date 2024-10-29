package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerUser(Model model) {

        model.addAttribute("customUser", new CustomUser()); // Must Reflect the name of our class
        model.addAttribute("userRoles", UserRole.values());

        return "register";
    }

    // TODO - Security Aspect for our Object
    // TODO - Aspects of DTO for UserCredentials & Validation
        // https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-initbinder.html#mvc-ann-initbinder-model-design
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute(name = "customUser") CustomUser customUser,   // Checks for constraints
            BindingResult bindingResult    // Check for error handling
    ) {

        if (bindingResult.hasErrors()) {
            return "register";      // add for query params ?error=someRandomError
        }

        // TODO - Handle Duplicate Entries
        // TODO - UserRole (DYNAMIC)
        userRepository.save(
                new CustomUser(
                        customUser.getUsername(),
                        passwordEncoder.encode(customUser.getPassword()),
                        customUser.getUserRole(),
                        true,
                        true,
                        true,
                        true
                )
        );

        return "redirect:/";
    }

}
