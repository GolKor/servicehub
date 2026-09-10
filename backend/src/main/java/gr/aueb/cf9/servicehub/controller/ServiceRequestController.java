package gr.aueb.cf9.servicehub.controller;

import gr.aueb.cf9.servicehub.dto.ServiceRequestCreateDTO;
import gr.aueb.cf9.servicehub.dto.ServiceRequestResponseDTO;
import gr.aueb.cf9.servicehub.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class ServiceRequestController {

    private final ServiceRequestService requestService;

    public ServiceRequestController(ServiceRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<ServiceRequestResponseDTO> createRequest(
            Authentication authentication,
            @Valid @RequestBody ServiceRequestCreateDTO dto) {

        ServiceRequestResponseDTO created = requestService.createRequest(authentication.getName(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestResponseDTO> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.getRequestById(id));
    }

    @GetMapping
    public ResponseEntity<List<ServiceRequestResponseDTO>> browseOpenRequests(
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(requestService.browseOpenRequests(categoryId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ServiceRequestResponseDTO>> getMyRequests(Authentication authentication) {
        return ResponseEntity.ok(requestService.getMyRequests(authentication.getName()));
    }
}