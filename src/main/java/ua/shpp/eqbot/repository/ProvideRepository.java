package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.shpp.eqbot.model.ProviderEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProvideRepository extends JpaRepository<ProviderEntity, Long> {
//    @Query("select t from ProviderEntity t where t. id = ?1")
//    Optional<ProviderEntity> findById(Long id);


    Optional<ProviderEntity> findById(Long id);

    @Query("select t from ProviderEntity t where t. idTelegram = ?1")
    List<ProviderEntity> findAllByIdTelegram(Long id);

    @Query("select t from ProviderEntity t where t.id = ?1 and t.city = ?2")
    Optional<ProviderEntity> findPleaseProviderEntitiesByIdTelegramAAndCity(Long id, String nameCity);

}
