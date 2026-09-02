package gr.aueb.cf9.servicehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ServiceCategoryRequestDTO(

        @NotBlank(message = "Category name must not be blank.")
        @Size(min = 2, max = 30, message = "Category name must be between 2 and 30 characters.")
        String name,

        @Size(max = 300, message = "Description must not exceed 300 characters.")
        String description
) {}