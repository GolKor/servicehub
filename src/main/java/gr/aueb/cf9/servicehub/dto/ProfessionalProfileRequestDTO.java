package gr.aueb.cf9.servicehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ProfessionalProfileRequestDTO(

        @NotBlank(message = "Business name must not be blank.")
        @Size(min = 2, max = 100, message = "Business name must be between 2 and 100 characters.")
        String businessName,

        @Size(max = 1000, message = "Description must not exceed 1000 characters.")
        String description,

        @NotBlank(message = "Location must not be blank.")
        String location,

        @NotBlank(message = "Phone must not be blank.")
        String phone,

        Integer experienceYears,

        Set<Long> categoryIds
) {}