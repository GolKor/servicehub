package gr.aueb.cf9.servicehub.controller;

import gr.aueb.cf9.servicehub.dto.BookingResponseDTO;
import gr.aueb.cf9.servicehub.dto.OfferCreateDTO;
import gr.aueb.cf9.servicehub.dto.OfferResponseDTO;
import gr.aueb.cf9.servicehub.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<OfferResponseDTO> createOffer(
            Authentication authentication,
            @Valid @RequestBody OfferCreateDTO dto) {

        OfferResponseDTO created = offerService.createOffer(authentication.getName(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/me")
    public ResponseEntity<List<OfferResponseDTO>> getMyOffers(Authentication authentication) {
        return ResponseEntity.ok(offerService.getMyOffers(authentication.getName()));
    }

    @PatchMapping("/{offerId}/accept")
    public ResponseEntity<BookingResponseDTO> acceptOffer(
            Authentication authentication,
            @PathVariable Long offerId) {

        BookingResponseDTO booking = offerService.acceptOffer(authentication.getName(), offerId);
        return ResponseEntity.ok(booking);
    }
}