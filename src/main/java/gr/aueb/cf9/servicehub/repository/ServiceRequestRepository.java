package gr.aueb.cf9.servicehub.repository;

import gr.aueb.cf9.servicehub.model.entity.ServiceRequest;
import gr.aueb.cf9.servicehub.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findByStatus(RequestStatus status);

    List<ServiceRequest> findByCategoryId(Long categoryId);

    List<ServiceRequest> findByCustomerId(Long customerId);

    List<ServiceRequest> findByStatusAndCategoryId(RequestStatus status, Long categoryId);
}