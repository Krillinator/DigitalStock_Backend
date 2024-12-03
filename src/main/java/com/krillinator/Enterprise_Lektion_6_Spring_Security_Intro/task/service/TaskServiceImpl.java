package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.service;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.DatabaseUtils;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.SecurityUtils;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.records.UserExistenceResult;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.Task;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.repository.TaskRepository;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.DatabaseUtils.UserExistenceStatus.*;

@Service
@Transactional
public class TaskServiceImpl implements ITaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public DatabaseUtils.UserExistenceStatus shareTaskWithUser(
            Long taskId,
            String ownerUsername,
            String partnerUsername
    ) {

        System.out.println("----------------------------");
        System.out.println("INSIDE SHARE TASK WITH USER");
        System.out.println("----------------------------");

        // Validate if the current logged-in user matches the owner
        if (!Objects.equals(SecurityUtils.getCurrentLoggedInUsername(), ownerUsername)) {
            return ILLEGAL_OPERATION;
        }

        // Check user existence ENUM status
        UserExistenceResult result = DatabaseUtils.checkUsersExistence(
                ownerUsername,
                partnerUsername,
                userRepository
        );

        DatabaseUtils.UserExistenceStatus status = result.status();

         return switch (status) {
            case BOTH_USERS_EXIST -> {

                // Check id existence
                Optional<Task> optionalTask = taskRepository.findById(taskId);
                if (optionalTask.isPresent()) {

                    // Convert & Set
                    Task task = optionalTask.get();
                    CustomUser partner = result.partner();

                    Optional<Task> duplicateTask = partner.getTaskList().stream().filter(
                            t -> Objects.equals(t.getId(), taskId)
                    ).findFirst();

                    if (duplicateTask.isPresent()) {
                        yield DUPLICATE_TASK_FOUND;
                    }

                    partner.getTaskList().add(task);
                    userRepository.save(partner);

                    System.out.println("User after adding: " + task.getCustomUser());
                    System.out.println("DEBUGGING");

                    yield BOTH_USERS_EXIST;
                }

                yield TASK_NOT_FOUND;
            }

             case OWNER_NOT_FOUND -> OWNER_NOT_FOUND;
             case PARTNER_NOT_FOUND -> PARTNER_NOT_FOUND;

             default -> ILLEGAL_OPERATION;
        };

    }

}
