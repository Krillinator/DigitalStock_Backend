package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.authorities;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.authorities.UserPermission.*;

public enum UserRole {

    GUEST(GET),
    USER(GET, POST),
    ADMIN(GET, POST, DELETE);

    private final List<String> permission;

    // Improved varargs for dynamic params
    UserRole(UserPermission... permissionList) {
        this.permission = Arrays.stream(permissionList)
                .map(UserPermission::getPermission)
                .toList();
    }

    public List<String> getListOfPermissions() {
        return permission;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();

        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));  // Springs Requirement for Authority Role (ROLE_)
        simpleGrantedAuthorityList.addAll(getListOfPermissions().stream().map(SimpleGrantedAuthority::new).toList());

        return simpleGrantedAuthorityList;
    }

}
