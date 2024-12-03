package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.service;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.DatabaseUtils;

public interface ITaskService {

    DatabaseUtils.UserExistenceStatus shareTaskWithUser(
            Long taskId, String ownerUsername, String partnerUsername
    );

}
