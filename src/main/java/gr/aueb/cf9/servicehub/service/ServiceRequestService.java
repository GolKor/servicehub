package gr.aueb.cf9.servicehub.service;

import gr.aueb.cf9.servicehub.core.exceptions.EntityNotFoundException;
import gr.aueb.cf9.servicehub.dto.ServiceRequestCreateDTO;
import gr.aueb.cf9.servicehub.dto.ServiceRequestResponseDTO;
import gr.aueb.cf9.servicehub.mapper.Mapper;
import gr.aueb.cf9.servicehub.model.entity.ServiceCategory;
import gr.aueb.cf9.servicehub.model.entity.ServiceRequest;
import gr.aueb.cf9.servicehub.model.entity.User;
import gr.aueb.cf9.servicehub.model.enums.RequestStatus;
import gr.aueb.cf9.servicehub.repository.ServiceCategoryRepository;
import gr.aueb.cf9.servicehub.repository.ServiceRequestRepository;
import gr.aueb.cf9.servicehub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final Mapper mapper;

    public ServiceRequestService(ServiceRequestRepository requestRepository,
                                 UserRepository userRepository,
                                 ServiceCategoryRepository categoryRepository,
                                 Mapper mapper) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public ServiceRequestResponseDTO createRequest(String customerEmail, ServiceRequestCreateDTO dto) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + customerEmail));

        ServiceCategory category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + dto.categoryId()));

        ServiceRequest request = new ServiceRequest();
        request.setTitle(dto.title());
        request.setDescription(dto.description());
        request.setLocation(dto.location());
        request.setPreferredDate(dto.preferredDate());
        request.setBudgetMin(dto.budgetMin());
        request.setBudgetMax(dto.budgetMax());
        request.setStatus(RequestStatus.OPEN);
        request.setCustomer(customer);
        request.setCategory(category);

        ServiceRequest saved = requestRepository.save(request);
        return mapper.toResponseDTO(saved);
    }

    public ServiceRequestResponseDTO getRequestById(Long id) {
        ServiceRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service request not found with id: " + id));
        return mapper.toResponseDTO(request);
    }

    public List<ServiceRequestResponseDTO> browseOpenRequests(Long categoryId) {
        List<ServiceRequest> requests = (categoryId != null)
                ? requestRepository.findByStatusAndCategoryId(RequestStatus.OPEN, categoryId)
                : requestRepository.findByStatus(RequestStatus.OPEN);

        return requests.stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public List<ServiceRequestResponseDTO> getMyRequests(String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + customerEmail));

        return requestRepository.findByCustomerId(customer.getId()).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}