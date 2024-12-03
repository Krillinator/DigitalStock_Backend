package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.records;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.DatabaseUtils;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;

public record UserExistenceResult(
        DatabaseUtils.UserExistenceStatus status,
        CustomUser owner,
        CustomUser partner) {

}
