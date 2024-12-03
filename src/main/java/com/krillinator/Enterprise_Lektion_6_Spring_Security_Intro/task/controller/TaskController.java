package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.controller;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.DatabaseUtils;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.SecurityUtils;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller.rest.AbstractApiRestController;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.Task;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.repository.TaskRepository;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.service.ITaskService;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class TaskController extends AbstractApiRestController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ITaskService iTaskService;

    @Autowired
    public TaskController(UserRepository userRepository, TaskRepository taskRepository, ITaskService iTaskService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.iTaskService = iTaskService;
    }

    @GetMapping("/test")
    public String testMapping() {

        return "You're logged in for sure";
    }

    @GetMapping("/user-tasks/{username}")
    public ResponseEntity<List<Task>> getTasksForUser(@PathVariable String username) {
        Optional<CustomUser> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // User not found
        }

        // Get tasks for the user
        CustomUser user = optionalUser.get();
        List<Task> tasks = user.getTaskList();

        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build(); // No tasks found for the user
        }

        return ResponseEntity.ok(tasks); // Return tasks
    }

    /**
     * Shares a task with a partner user.
     *
     * @param taskId          The ID of the task to be shared.
     * @param ownerUsername   The username of the task owner.
     * @param partnerUsername The username of the partner user to share the task with.
     * @return ResponseEntity with appropriate status and message.
     */
    @PostMapping("/share/{taskId}/{ownerUsername}/{partnerUsername}")
    public ResponseEntity<String> shareTaskWithUser(@PathVariable Long taskId,
                                                    @PathVariable String ownerUsername,
                                                    @PathVariable String partnerUsername) {

        // Validate if the current logged-in user matches the owner
        if (!Objects.equals(SecurityUtils.getCurrentLoggedInUsername(), ownerUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden: You are not the owner");
        }

        // Call service to share the task and get the result
        DatabaseUtils.UserExistenceStatus status = iTaskService.shareTaskWithUser(taskId, ownerUsername, partnerUsername);

        // Map the enum status to appropriate response
        return switch (status) {
            case BOTH_USERS_EXIST ->    ResponseEntity.status(HttpStatus.CREATED).body("Task successfully shared with partner");
            case OWNER_NOT_FOUND ->     ResponseEntity.status(HttpStatus.NOT_FOUND).body("Owner username not found");
            case PARTNER_NOT_FOUND ->   ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner username not found");
            case TASK_NOT_FOUND ->      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
            case DUPLICATE_TASK_FOUND ->ResponseEntity.status(HttpStatus.CONFLICT).body("Task not found");

            default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected error occurred");
        };
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

        // Auth Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

        // Add the current user to the task's list (ensuring task is linked to the user)
        List<CustomUser> userList = task.getCustomUser();
        if (userList == null) {
            userList = new ArrayList<>();
        }
        userList.add(customUser);  // Ensure this task is associated with the current user
        task.setCustomUser(userList);

        // Save the task (cascade will handle saving the user side)
        taskRepository.save(task);  // This is the only save needed

        // Add the new task to the user's existing task list
        List<Task> currentTaskList = customUser.getTaskList();
        if (currentTaskList == null) {
            currentTaskList = new ArrayList<>();
        }
        currentTaskList.add(task);  // Append the new task to the existing list
        customUser.setTaskList(currentTaskList);

        // Save the user (no need to save the task, it's already saved)
        userRepository.save(customUser);  // Only saving the owning side is needed

        return ResponseEntity.status(201).body("Success!");
    }



}
