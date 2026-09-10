package gr.aueb.cf9.servicehub.controller;

import gr.aueb.cf9.servicehub.dto.BookingResponseDTO;
import gr.aueb.cf9.servicehub.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(Authentication authentication) {
        return ResponseEntity.ok(bookingService.getMyBookingsAsCustomer(authentication.getName()));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(authentication.getName(), id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<BookingResponseDTO> completeBooking(
            Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.completeBooking(authentication.getName(), id));
    }
}