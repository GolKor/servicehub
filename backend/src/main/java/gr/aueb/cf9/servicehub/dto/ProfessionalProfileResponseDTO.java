package gr.aueb.cf9.servicehub.dto;

import java.util.Set;

public record ProfessionalProfileResponseDTO(
        Long id,
        String businessName,
        String description,
        String location,
        String phone,
        Integer experienceYears,
        Double rating,
        Set<ServiceCategoryResponseDTO> categories,
        Long userId
) {}