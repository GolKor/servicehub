package gr.aueb.cf9.servicehub.repository;

import gr.aueb.cf9.servicehub.model.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
}