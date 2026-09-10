package gr.aueb.cf9.servicehub.dto;

import java.time.LocalDateTime;

public record ReviewResponseDTO(
        Long id,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        Long bookingId,
        UserSummaryDTO customer,
        ProfessionalSummaryDTO professional
) {}