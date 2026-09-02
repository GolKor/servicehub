package gr.aueb.cf9.servicehub.dto;

import gr.aueb.cf9.servicehub.model.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingResponseDTO(
        Long id,
        LocalDateTime scheduledDate,
        BookingStatus status,
        LocalDateTime createdAt,
        Long offerId,
        UserSummaryDTO customer,
        ProfessionalSummaryDTO professional
) {}