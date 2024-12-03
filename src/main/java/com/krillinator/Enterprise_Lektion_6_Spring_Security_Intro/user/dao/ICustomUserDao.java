package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.dao;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;

import java.util.List;

public interface ICustomUserDao {

    List<CustomUser> filterAllUsersByTask(Long taskId);

}
