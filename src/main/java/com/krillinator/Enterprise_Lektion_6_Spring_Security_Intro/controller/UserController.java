package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO - Check for CustomUser as an alternate approach for Model model as args
    @GetMapping("/register")
    public String registerUser(Model model) {

        model.addAttribute("user", new CustomUser());

        return "register";
    }

    // TODO - This does not work, cause: permission?
    @PostMapping("/register")
    public String registerUser(
            @Valid CustomUser customUser,   // Checks for constraints
            BindingResult bindingResult,    // Check for error handling
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "register";      // add for query params ?error=someRandomError
        }

        userRepository.save(
                new CustomUser(
                        customUser.getUsername(),
                        customUser.getPassword(),
                        UserRole.ADMIN,
                        true,
                        true,
                        true,
                        true
                )
        );

        return "redirect:/";
    }

}
