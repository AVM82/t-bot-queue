package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shpp.eqbot.model.RegistrationForTheServiceEntity;

import java.util.List;

@Repository
public interface RegistrationForTheServiceRepository extends JpaRepository<RegistrationForTheServiceEntity, Long> {
    //List<RegistrationForTheServiceEntity> findAllBy(Long id);
}
