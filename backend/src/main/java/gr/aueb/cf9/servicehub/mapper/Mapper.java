package gr.aueb.cf9.servicehub.mapper;

import gr.aueb.cf9.servicehub.dto.*;
import gr.aueb.cf9.servicehub.model.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public ProfessionalProfileResponseDTO toResponseDTO(ProfessionalProfile profile) {
        Set<ServiceCategoryResponseDTO> categoryDTOs = profile.getCategories().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toSet());

        return new ProfessionalProfileResponseDTO(
                profile.getId(),
                profile.getBusinessName(),
                profile.getDescription(),
                profile.getLocation(),
                profile.getPhone(),
                profile.getExperienceYears(),
                profile.getRating(),
                categoryDTOs,
                profile.getUser().getId()
        );
    }

    public ProfessionalSummaryDTO toSummaryDTO(ProfessionalProfile profile) {
        return new ProfessionalSummaryDTO(
                profile.getId(),
                profile.getBusinessName(),
                profile.getRating()
        );
    }

    public UserSummaryDTO toSummaryDTO(User user) {
        return new UserSummaryDTO(user.getId(), user.getName());
    }

    public OfferResponseDTO toResponseDTO(Offer offer) {
        return new OfferResponseDTO(
                offer.getId(),
                offer.getPrice(),
                offer.getProposedDate(),
                offer.getMessage(),
                offer.getStatus(),
                offer.getCreatedAt(),
                toSummaryDTO(offer.getProfessional()),
                offer.getServiceRequest().getId()
        );
    }

    public ServiceRequestResponseDTO toResponseDTO(ServiceRequest request) {
        List<OfferResponseDTO> offerDTOs = request.getOffers().stream()
                .map(this::toResponseDTO)
                .toList();

        return new ServiceRequestResponseDTO(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getPreferredDate(),
                request.getBudgetMin(),
                request.getBudgetMax(),
                request.getStatus(),
                request.getCreatedAt(),
                toSummaryDTO(request.getCustomer()),
                toResponseDTO(request.getCategory()),
                offerDTOs
        );
    }

    public BookingResponseDTO toResponseDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getScheduledDate(),
                booking.getStatus(),
                booking.getCreatedAt(),
                booking.getOffer().getId(),
                toSummaryDTO(booking.getCustomer()),
                toSummaryDTO(booking.getProfessional())
        );
    }

    public ReviewResponseDTO toResponseDTO(Review review) {
        Booking booking = review.getBooking();
        return new ReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                booking.getId(),
                toSummaryDTO(booking.getCustomer()),
                toSummaryDTO(booking.getProfessional())
        );
    }
}