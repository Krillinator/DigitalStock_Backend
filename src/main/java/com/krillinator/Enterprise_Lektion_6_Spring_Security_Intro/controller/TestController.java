package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config.AppPasswordConfig;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public TestController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/hash")
    public String testHash() {

        // TODO - We want to SEE whether it's default bCryptPasswordEncoder encoder or OUR BEAN
        // TODO - We do NOT use our BEAN?!

        return passwordEncoder.encode("123");
    }

    // TODO - Query for already existing users with same credentials
    // TODO - Check if UserRole.ADMIN correctly sets Authorities

    @GetMapping("/createDefaultUser")
    public CustomUser createDefaultUser() {

        CustomUser customUser = new CustomUser(
                "Benny",
                passwordEncoder.encode("123"),
                UserRole.ADMIN,
                true,
                true,
                true,
                true
        );

        return userRepository.save(customUser);
    }



}

