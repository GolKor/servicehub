package gr.aueb.cf9.servicehub.repository;

import gr.aueb.cf9.servicehub.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByBookingId(Long bookingId);

    @Query("SELECT r FROM Review r WHERE r.booking.offer.professional.id = :professionalId")
    List<Review> findByProfessionalId(@Param("professionalId") Long professionalId);
}