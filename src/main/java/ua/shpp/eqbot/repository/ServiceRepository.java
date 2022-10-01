package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.eqbot.model.ServiceEntity;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    @Query("select t from ServiceEntity t where t.name = ?1 and t.id=?2")
    ServiceEntity getFirstByNameAndId(String name, Long id);

    @Query("select t from ServiceEntity t where t. id = ?1")
    List<ServiceEntity> findAllById(Long id);

}
