package gr.aueb.cf9.servicehub.service;

import gr.aueb.cf9.servicehub.dto.ServiceCategoryRequestDTO;
import gr.aueb.cf9.servicehub.dto.ServiceCategoryResponseDTO;
import gr.aueb.cf9.servicehub.mapper.Mapper;
import gr.aueb.cf9.servicehub.model.entity.ServiceCategory;
import gr.aueb.cf9.servicehub.repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCategoryService {

    private final ServiceCategoryRepository categoryRepository;
    private final Mapper mapper;

    public ServiceCategoryService(ServiceCategoryRepository categoryRepository,
                                  Mapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public List<ServiceCategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public ServiceCategoryResponseDTO getCategoryById(Long id) {
        ServiceCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return mapper.toResponseDTO(category);
    }

    public ServiceCategoryResponseDTO createCategory(ServiceCategoryRequestDTO dto) {
        ServiceCategory category = mapper.toEntity(dto);
        ServiceCategory saved = categoryRepository.save(category);
        return mapper.toResponseDTO(saved);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}