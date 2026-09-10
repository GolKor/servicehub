package gr.aueb.cf9.servicehub.service;

import gr.aueb.cf9.servicehub.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf9.servicehub.core.exceptions.EntityNotFoundException;
import gr.aueb.cf9.servicehub.dto.BookingResponseDTO;
import gr.aueb.cf9.servicehub.mapper.Mapper;
import gr.aueb.cf9.servicehub.model.entity.Booking;
import gr.aueb.cf9.servicehub.model.enums.BookingStatus;
import gr.aueb.cf9.servicehub.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final Mapper mapper;

    public BookingService(BookingRepository bookingRepository, Mapper mapper) {
        this.bookingRepository = bookingRepository;
        this.mapper = mapper;
    }

    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        return mapper.toResponseDTO(booking);
    }

    public BookingResponseDTO cancelBooking(String userEmail, Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

        // Ασφάλεια: μόνο customer ή professional του booking μπορεί να το ακυρώσει
        boolean isParticipant = booking.getCustomer().getEmail().equals(userEmail)
                || booking.getProfessional().getUser().getEmail().equals(userEmail);
        if (!isParticipant) {
            throw new EntityInvalidArgumentException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() != BookingStatus.SCHEDULED) {
            throw new EntityInvalidArgumentException("Only SCHEDULED bookings can be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        return mapper.toResponseDTO(saved);
    }

    public BookingResponseDTO completeBooking(String professionalEmail, Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

        // Ασφάλεια: μόνο ο professional του booking μπορεί να το ολοκληρώσει
        if (!booking.getProfessional().getUser().getEmail().equals(professionalEmail)) {
            throw new EntityInvalidArgumentException("You are not authorized to complete this booking");
        }

        if (booking.getStatus() != BookingStatus.SCHEDULED
                && booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new EntityInvalidArgumentException("Only SCHEDULED or IN_PROGRESS bookings can be completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        Booking saved = bookingRepository.save(booking);
        return mapper.toResponseDTO(saved);
    }

    public List<BookingResponseDTO> getMyBookingsAsCustomer(String customerEmail) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getCustomer().getEmail().equals(customerEmail))
                .map(mapper::toResponseDTO)
                .toList();
    }
}