package gr.aueb.cf9.servicehub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "Email must not be blank.")
        @Email(message = "Invalid email format.")
        String email,

        @NotBlank(message = "Password must not be blank.")
        String password
) {}