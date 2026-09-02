package gr.aueb.cf9.servicehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OfferCreateDTO(

        @NotNull(message = "Price must not be null.")
        BigDecimal price,

        LocalDateTime proposedDate,

        @NotBlank(message = "Message must not be blank.")
        String message,

        @NotNull(message = "Service request must not be null.")
        Long serviceRequestId
) {}