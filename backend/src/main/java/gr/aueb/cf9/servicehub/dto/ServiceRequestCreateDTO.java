package gr.aueb.cf9.servicehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ServiceRequestCreateDTO(

        @NotBlank(message = "Title must not be blank.")
        @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters.")
        String title,

        @Size(max = 1000, message = "Description must not exceed 1000 characters.")
        String description,

        @NotBlank(message = "Location must not be blank.")
        String location,

        LocalDate preferredDate,

        BigDecimal budgetMin,

        BigDecimal budgetMax,

        @NotNull(message = "Category must not be null.")
        Long categoryId
) {}