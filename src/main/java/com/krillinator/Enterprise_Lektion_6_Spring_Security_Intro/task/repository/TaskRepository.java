package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.repository;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
