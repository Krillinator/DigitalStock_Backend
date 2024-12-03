package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller.rest;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.authorities.UserRole;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.Task;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.dto.CustomUserDTO;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.repository.TaskRepository;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TestController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TestController(PasswordEncoder passwordEncoder, UserRepository userRepository, TaskRepository taskRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/hash")
    public String testHash() {

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

    @PostMapping("/users")
    public ResponseEntity<CustomUser> createNewUser(
            @Valid @RequestBody CustomUser customUser,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(customUser);
    }

    @GetMapping("/get-my-username")
    public ResponseEntity<CustomUserDTO> getCurrentUsername() {

        Optional<CustomUser> customUser = userRepository.findByUsername("Benny");

        if (customUser.isPresent()) {
            CustomUserDTO customUserDTO = new CustomUserDTO(customUser.get());

            return ResponseEntity.ok().body(customUserDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/task")
    public ResponseEntity<String> createTask(
            @Valid @RequestBody Task task,
            BindingResult bindingResult
    ) {

        // Validation (Max chars/Not Blank)
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        // TODO - Move following three lines to a helper method (Service impl.)
        // Auth Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Currently Logged-in user
        String username = authentication.getName();

        // Check for user in DB
        Optional<CustomUser> currentUser = userRepository.findByUsername(username);

        System.out.println("---DEBUGGING---");
        System.out.println(username);
        System.out.println("---DEBUGGING---");

        // Check if User is NULL
        if (currentUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Set User Data
        CustomUser customUser = currentUser.get();
        List<CustomUser> userList = new ArrayList<>(List.of(customUser));

        // Connect User -> Task
        task.setCustomUser(userList);

        // Save Task
        Task savedTask = taskRepository.save(task);

        // Prepare Data
        List<Task> taskList = new ArrayList<>(List.of(savedTask));

        // Connect Task -> User
        customUser.setTaskList(taskList);
        userRepository.save(customUser);

        return ResponseEntity.status(201).body("Success!");
    }

}














