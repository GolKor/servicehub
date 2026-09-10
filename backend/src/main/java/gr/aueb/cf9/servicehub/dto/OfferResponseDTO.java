package gr.aueb.cf9.servicehub.dto;

import gr.aueb.cf9.servicehub.model.enums.OfferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OfferResponseDTO(
        Long id,
        BigDecimal price,
        LocalDateTime proposedDate,
        String message,
        OfferStatus status,
        LocalDateTime createdAt,
        ProfessionalSummaryDTO professional,
        Long serviceRequestId
) {}