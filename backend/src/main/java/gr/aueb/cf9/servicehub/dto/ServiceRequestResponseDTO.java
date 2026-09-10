package gr.aueb.cf9.servicehub.dto;

import gr.aueb.cf9.servicehub.model.enums.RequestStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ServiceRequestResponseDTO(
        Long id,
        String title,
        String description,
        String location,
        LocalDate preferredDate,
        BigDecimal budgetMin,
        BigDecimal budgetMax,
        RequestStatus status,
        LocalDateTime createdAt,
        UserSummaryDTO customer,
        ServiceCategoryResponseDTO category,
        List<OfferResponseDTO> offers
) {}