package gr.aueb.cf9.servicehub.service;

import gr.aueb.cf9.servicehub.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf9.servicehub.core.exceptions.EntityNotFoundException;
import gr.aueb.cf9.servicehub.dto.BookingResponseDTO;
import gr.aueb.cf9.servicehub.dto.OfferCreateDTO;
import gr.aueb.cf9.servicehub.dto.OfferResponseDTO;
import gr.aueb.cf9.servicehub.mapper.Mapper;
import gr.aueb.cf9.servicehub.model.entity.*;
import gr.aueb.cf9.servicehub.model.enums.BookingStatus;
import gr.aueb.cf9.servicehub.model.enums.OfferStatus;
import gr.aueb.cf9.servicehub.model.enums.RequestStatus;
import gr.aueb.cf9.servicehub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final ServiceRequestRepository requestRepository;
    private final ProfessionalProfileRepository professionalRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final Mapper mapper;

    public OfferService(OfferRepository offerRepository,
                        ServiceRequestRepository requestRepository,
                        ProfessionalProfileRepository professionalRepository,
                        UserRepository userRepository,
                        BookingRepository bookingRepository,
                        Mapper mapper) {
        this.offerRepository = offerRepository;
        this.requestRepository = requestRepository;
        this.professionalRepository = professionalRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.mapper = mapper;
    }

    public OfferResponseDTO createOffer(String professionalEmail, OfferCreateDTO dto) {
        User user = userRepository.findByEmail(professionalEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + professionalEmail));

        ProfessionalProfile professional = professionalRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Professional profile not found for this user"));

        ServiceRequest request = requestRepository.findById(dto.serviceRequestId())
                .orElseThrow(() -> new EntityNotFoundException("Service request not found with id: " + dto.serviceRequestId()));

        // Rule 3: Ένας professional δεν μπορεί να κάνει offer στο δικό του request
        if (request.getCustomer().getId().equals(user.getId())) {
            throw new EntityInvalidArgumentException("You cannot make an offer on your own service request");
        }

        // Rule 5: Δεν μπορεί να δημιουργηθεί offer σε CANCELLED ή CLOSED request
        if (request.getStatus() != RequestStatus.OPEN) {
            throw new EntityInvalidArgumentException("Cannot make an offer on a request that is not OPEN");
        }

        Offer offer = new Offer();
        offer.setPrice(dto.price());
        offer.setProposedDate(dto.proposedDate());
        offer.setMessage(dto.message());
        offer.setStatus(OfferStatus.PENDING);
        offer.setProfessional(professional);
        offer.setServiceRequest(request);

        Offer saved = offerRepository.save(offer);
        return mapper.toResponseDTO(saved);
    }

    public List<OfferResponseDTO> getMyOffers(String professionalEmail) {
        User user = userRepository.findByEmail(professionalEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + professionalEmail));

        ProfessionalProfile professional = professionalRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Professional profile not found for this user"));

        return offerRepository.findByProfessionalId(professional.getId()).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public BookingResponseDTO acceptOffer(String customerEmail, Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("Offer not found with id: " + offerId));

        ServiceRequest request = offer.getServiceRequest();

        // Ασφάλεια: μόνο ο owner του request μπορεί να κάνει accept
        if (!request.getCustomer().getEmail().equals(customerEmail)) {
            throw new EntityInvalidArgumentException("You are not authorized to accept this offer");
        }

        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new EntityInvalidArgumentException("Only PENDING offers can be accepted");
        }

        // Rule 6: το επιλεγμένο offer γίνεται ACCEPTED, τα υπόλοιπα REJECTED
        List<Offer> allOffers = offerRepository.findByServiceRequestId(request.getId());
        for (Offer o : allOffers) {
            if (o.getId().equals(offer.getId())) {
                o.setStatus(OfferStatus.ACCEPTED);
            } else if (o.getStatus() == OfferStatus.PENDING) {
                o.setStatus(OfferStatus.REJECTED);
            }
        }
        offerRepository.saveAll(allOffers);

        // Rule 7: δημιουργείται Booking
        Booking booking = new Booking();
        booking.setOffer(offer);
        booking.setScheduledDate(offer.getProposedDate());
        booking.setStatus(BookingStatus.SCHEDULED);
        Booking savedBooking = bookingRepository.save(booking);

        // Το ServiceRequest κλείνει, δεν δέχεται άλλα offers
        request.setStatus(RequestStatus.CLOSED);
        requestRepository.save(request);

        return mapper.toResponseDTO(savedBooking);
    }
}