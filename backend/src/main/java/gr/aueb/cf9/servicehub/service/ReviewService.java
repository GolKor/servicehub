package gr.aueb.cf9.servicehub.service;

import gr.aueb.cf9.servicehub.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf9.servicehub.core.exceptions.EntityNotFoundException;
import gr.aueb.cf9.servicehub.dto.ReviewCreateDTO;
import gr.aueb.cf9.servicehub.dto.ReviewResponseDTO;
import gr.aueb.cf9.servicehub.mapper.Mapper;
import gr.aueb.cf9.servicehub.model.entity.Booking;
import gr.aueb.cf9.servicehub.model.entity.Review;
import gr.aueb.cf9.servicehub.model.enums.BookingStatus;
import gr.aueb.cf9.servicehub.repository.BookingRepository;
import gr.aueb.cf9.servicehub.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final Mapper mapper;

    public ReviewService(ReviewRepository reviewRepository,
                         BookingRepository bookingRepository,
                         Mapper mapper) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.mapper = mapper;
    }

    public ReviewResponseDTO createReview(String customerEmail, ReviewCreateDTO dto) {
        Booking booking = bookingRepository.findById(dto.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + dto.bookingId()));

        // Ασφάλεια: μόνο ο customer του booking μπορεί να αφήσει review
        if (!booking.getCustomer().getEmail().equals(customerEmail)) {
            throw new EntityInvalidArgumentException("You are not authorized to review this booking");
        }

        // Rule 8: review μόνο μετά από COMPLETED booking
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new EntityInvalidArgumentException("You can only review a completed booking");
        }

        // Rule 9: ένα booking μπορεί να έχει μόνο ένα review
        if (reviewRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new EntityInvalidArgumentException("This booking has already been reviewed");
        }

        Review review = new Review();
        review.setRating(dto.rating());
        review.setComment(dto.comment());
        review.setBooking(booking);

        Review saved = reviewRepository.save(review);
        return mapper.toResponseDTO(saved);
    }

    public List<ReviewResponseDTO> getReviewsByProfessional(Long professionalId) {
        return reviewRepository.findByProfessionalId(professionalId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}