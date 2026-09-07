package gr.aueb.cf9.servicehub.controller;

import gr.aueb.cf9.servicehub.dto.ProfessionalProfileRequestDTO;
import gr.aueb.cf9.servicehub.dto.ProfessionalProfileResponseDTO;
import gr.aueb.cf9.servicehub.service.ProfessionalProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalProfileController {

    private final ProfessionalProfileService profileService;

    public ProfessionalProfileController(ProfessionalProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/me")
    public ResponseEntity<ProfessionalProfileResponseDTO> createProfile(
            Authentication authentication,
            @Valid @RequestBody ProfessionalProfileRequestDTO dto) {

        String userEmail = authentication.getName();
        ProfessionalProfileResponseDTO created = profileService.createProfile(userEmail, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfessionalProfileResponseDTO> getMyProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        ProfessionalProfileResponseDTO profile = profileService.getMyProfile(userEmail);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<ProfessionalProfileResponseDTO> updateProfile(
            Authentication authentication,
            @Valid @RequestBody ProfessionalProfileRequestDTO dto) {

        String userEmail = authentication.getName();
        ProfessionalProfileResponseDTO updated = profileService.updateProfile(userEmail, dto);
        return ResponseEntity.ok(updated);
    }
}