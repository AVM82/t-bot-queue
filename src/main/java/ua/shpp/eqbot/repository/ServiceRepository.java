package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.eqbot.model.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    ServiceEntity getFirstByName(String name);

    @Query("select t from ServiceEntity t where t.name = ?1 and t.id_telegram=?2")
    ServiceEntity getFirstByNameAndAndId_telegram(String name, Long id_telegram);
}