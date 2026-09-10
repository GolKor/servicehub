package gr.aueb.cf9.servicehub.dto;

import gr.aueb.cf9.servicehub.model.enums.Role;
import jakarta.validation.constraints.*;

public record RegisterRequestDTO(

        @NotBlank(message = "Name must not be blank.")
        @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters.")
        String name,

        @NotBlank(message = "Email must not be blank.")
        @Email(message = "Invalid email format.")
        String email,

        @NotBlank(message = "Password must not be blank.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])^.{8,}$",
                message = "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 digit, and 1 special character, with no spaces.")
        String password,

        @NotNull(message = "Role must not be null.")
        Role role
) {}