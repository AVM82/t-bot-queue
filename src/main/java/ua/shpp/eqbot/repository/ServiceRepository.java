package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.eqbot.model.ServiceEntity;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    ServiceEntity getFirstByName(String name);

    @Query("select t from ServiceEntity t where t.name = ?1 and t.telegramId=?2")
    ServiceEntity getFirstByNameAndAndTelegramId(String name, Long telegramId);

    ServiceEntity findFirstById (Long id);

    @Query("select t from ServiceEntity t where t. telegramId = ?1")
    List<ServiceEntity> findAllByTelegramId(Long id);

    List<ServiceEntity> findAllByTelegramIdIn(List<Long> telegramIdList);
}
