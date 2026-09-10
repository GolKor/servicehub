package gr.aueb.cf9.servicehub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewCreateDTO(

        @NotNull(message = "Rating must not be null.")
        @Min(value = 1, message = "Rating must be at least 1.")
        @Max(value = 5, message = "Rating must not exceed 5.")
        Integer rating,

        @Size(max = 1000, message = "Comment must not exceed 1000 characters.")
        String comment,

        @NotNull(message = "Booking must not be null.")
        Long bookingId
) {}