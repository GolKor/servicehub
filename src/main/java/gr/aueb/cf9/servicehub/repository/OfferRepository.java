package gr.aueb.cf9.servicehub.repository;

import gr.aueb.cf9.servicehub.model.entity.Offer;
import gr.aueb.cf9.servicehub.model.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByServiceRequestId(Long serviceRequestId);

    List<Offer> findByProfessionalId(Long professionalId);

    List<Offer> findByServiceRequestIdAndStatus(Long serviceRequestId, OfferStatus status);
}