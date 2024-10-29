package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.authorities.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Entity
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @NotEmpty - Mostly for collections
    @NotBlank
    @Size(min = 4, max = 32, message = "Username must be between 4-32 chars")
    private String username;

    @NotBlank
    @Size(min = 7, max = 80, message = "Password must be between 7-80 chars")
    private String password;

    // TODO - Implement NotBlank possibly for <select> element?
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public CustomUser() {}
    public CustomUser(String username, String password, UserRole userRole, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Authorities include: ROLE & Permissions: ["ROLE_ADMIN", "GET", "DELETE"]
    // @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthorities() {
        return userRole.getAuthorities();
    }

    // Permissions include: ["GET", "DELETE"]
    // @JsonIgnore // userRepository.save() will print out these details otherwise
    public List<String> getListOfPermissions() {
        return userRole.getListOfPermissions();
    }

    // Role include: ADMIN (UserRole.name())
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
