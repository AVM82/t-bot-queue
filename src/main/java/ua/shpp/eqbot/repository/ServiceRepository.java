package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.eqbot.model.ServiceEntity;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    ServiceEntity getFirstByName(String name);

    @Query("select t from ServiceEntity t where t.name = ?1 and t.idTelegram=?2")
    ServiceEntity getFirstByNameAndAndIdTelegram(String name, Long idTelegram);

    ServiceEntity findFirstById (Long id);

    @Query("select t from ServiceEntity t where t. idTelegram = ?1")
    List<ServiceEntity> findAllByIdTelegram(Long id);

    List<ServiceEntity> findAllByIdTelegramIn(List<Long> idTelegramList);
}
