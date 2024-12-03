package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 32)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

    // TODO - Avoid including a USER in POSTMAN
    @ManyToMany(mappedBy = "taskList")
    @JsonIgnore
    private List<CustomUser> customUser;

    public List<CustomUser> getCustomUser() {
        return customUser;
    }

    public void setCustomUser(List<CustomUser> customUser) {
        this.customUser = customUser;
    }

    public Task() {}
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public @NotBlank @Size(max = 32) String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank @Size(max = 32) String title) {
        this.title = title;
    }

    public @NotBlank @Size(max = 2000) String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank @Size(max = 2000) String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", customUser=" + customUser +
                '}';
    }
}
