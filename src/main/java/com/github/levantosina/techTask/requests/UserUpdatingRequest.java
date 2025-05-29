package com.github.levantosina.techTask.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserUpdatingRequest (

        @NotBlank(message = "First name can not be empty")
        String firstName,
        @NotBlank(message = "Last name can not be empty")
        String lastName,
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "Invalid email format"
        )
        String email
) {
}
