package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.eqbot.model.ServiceEntity;

import java.util.List;
import java.util.Set;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    ServiceEntity getFirstByName(String name);

    @Query("select t from ServiceEntity t where t.name = ?1 and t.telegramId=?2")
    ServiceEntity getFirstByNameAndAndTelegramId(String name, Long telegramId);

    ServiceEntity findFirstById(Long id);

    @Query("select t from ServiceEntity t where t. telegramId = ?1")
    List<ServiceEntity> findAllByTelegramId(Long id);

    List<ServiceEntity> findAllByTelegramIdIn(List<Long> telegramIdList);

    @Query("select distinct t.name from ServiceEntity t ")
    Set<String> findAllByName();
//
//    @Query("SELECT * FROM services WHERE (services.description LIKE '%ремо%') OR (services.name LIKE '%ремо%');")
//    List<ServiceEntity> findAllByDescriptionAndName(String description, String name);
//
    List<ServiceEntity> findByDescriptionContaining(String description);
}
