package gr.aueb.cf9.servicehub.repository;

import gr.aueb.cf9.servicehub.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByOfferId(Long offerId);
}