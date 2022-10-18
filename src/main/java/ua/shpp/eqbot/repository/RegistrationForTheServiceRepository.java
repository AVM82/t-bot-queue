package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.shpp.eqbot.model.RegistrationForTheServiceEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistrationForTheServiceRepository extends JpaRepository<RegistrationForTheServiceEntity, Long> {
    @Query(value = "SELECT * FROM registration_for_the_user r WHERE r. service_id = ?1", nativeQuery = true)
    List<RegistrationForTheServiceEntity> findAllServicesById(Long id);

    @Query(value = "SELECT * FROM registration_for_the_user r WHERE r. service_registration_date_time > ?1 AND r. service_registration_date_time < ?2 AND r. service_id = ?3", nativeQuery = true)
    List<RegistrationForTheServiceEntity> findAllServicesByDateAndServiceId(LocalDateTime from, LocalDateTime to, Long serviceId);

    @Query("SELECT e FROM RegistrationForTheServiceEntity e WHERE e.serviceRegistrationDateTime >= ?1 AND e.serviceRegistrationDateTime < ?2 AND e.sentReminderDate is NULL")
    List<RegistrationForTheServiceEntity> findAllBetweenDates(LocalDateTime from, LocalDateTime until);
}
