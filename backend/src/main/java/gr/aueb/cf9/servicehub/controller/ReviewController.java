package gr.aueb.cf9.servicehub.controller;

import gr.aueb.cf9.servicehub.dto.ReviewCreateDTO;
import gr.aueb.cf9.servicehub.dto.ReviewResponseDTO;
import gr.aueb.cf9.servicehub.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(
            Authentication authentication,
            @Valid @RequestBody ReviewCreateDTO dto) {

        ReviewResponseDTO created = reviewService.createReview(authentication.getName(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProfessional(@PathVariable Long professionalId) {
        return ResponseEntity.ok(reviewService.getReviewsByProfessional(professionalId));
    }
}