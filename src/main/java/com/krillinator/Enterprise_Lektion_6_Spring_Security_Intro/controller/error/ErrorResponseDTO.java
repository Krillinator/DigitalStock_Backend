package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller.error;

public record ErrorResponseDTO(
        String description,
        String message
        // int statusCode
) {
}
