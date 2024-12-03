package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.dto;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;
import jakarta.validation.constraints.Size;

// Default = FINAL
// You CAN define Validation through DTO (spring boot starter validation)
// recommend to use Validation in DTO
// TODO - Separation of Concerns... --> Validation Groups
// TODO - 10:30 - Fortsätter vi! :)
public record CustomUserDTO(
        @Size(min = 2, max = 8) String username
) {

    // Custom Constructor
    public CustomUserDTO(CustomUser customUser) {
        this(customUser.getUsername());
    }

}

