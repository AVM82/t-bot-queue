package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.eqbot.model.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
}