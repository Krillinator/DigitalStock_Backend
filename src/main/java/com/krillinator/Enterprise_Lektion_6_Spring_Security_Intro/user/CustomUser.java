package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.authorities.UserRole;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

// TODO - Validation for getters/setters?

@Entity
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @NotEmpty - Mostly for collections
    @NotBlank
    @Size(min = 4, max = 32, message = "Username must be between 4-32 chars")
    private String username;

    // TODO - What the database can accept VS what can be written in the form
    @NotBlank
    @Size(min = 7, max = 80, message = "Password must be between 7-80 chars")
    private String password;

    // TODO - Implement NotBlank possibly for <select> element?
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is Required") private UserRole userRole;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "task_list",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> taskList = new ArrayList<>();

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

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
