package gr.aueb.cf9.servicehub.dto;

import gr.aueb.cf9.servicehub.model.enums.Role;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Role role,
        boolean enabled,
        LocalDateTime createdAt
) {}