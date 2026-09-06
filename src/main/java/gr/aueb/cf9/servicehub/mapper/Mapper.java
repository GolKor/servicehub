package gr.aueb.cf9.servicehub.mapper;

import gr.aueb.cf9.servicehub.dto.ServiceCategoryRequestDTO;
import gr.aueb.cf9.servicehub.dto.ServiceCategoryResponseDTO;
import gr.aueb.cf9.servicehub.dto.UserResponseDTO;
import gr.aueb.cf9.servicehub.model.entity.ServiceCategory;
import gr.aueb.cf9.servicehub.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public ServiceCategoryResponseDTO toResponseDTO(ServiceCategory category) {
        return new ServiceCategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public ServiceCategory toEntity(ServiceCategoryRequestDTO dto) {
        ServiceCategory category = new ServiceCategory();
        category.setName(dto.name());
        category.setDescription(dto.description());
        return category;
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
}