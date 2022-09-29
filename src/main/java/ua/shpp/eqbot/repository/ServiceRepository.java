package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.eqbot.model.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    @Query("select t from ServiceEntity t where t. id_telegram = ?1")
    ServiceEntity findByIdTelegram(Long id);
}