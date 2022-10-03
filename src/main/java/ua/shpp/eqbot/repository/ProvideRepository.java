package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.shpp.eqbot.model.ProviderEntity;

import javax.ws.rs.DELETE;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProvideRepository extends JpaRepository<ProviderEntity, Long> {
    @Query("select t from ProviderEntity t where t. idTelegram = ?1")
    ProviderEntity findFirstByIdTelegram(Long id);

    @Query("select t from ProviderEntity t where t. idTelegram = ?1")
    ProviderEntity findByIdTelegram(Long id);

    @Query("select t from ProviderEntity t where t. idTelegram = ?1")
    List<ProviderEntity> findAllByIdTelegram(Long id);

    Optional<ProviderEntity> findProviderEntitiesByIdTelegramAndName(Long idTelegram, String name);

//    ProviderEntity findPleaseProviderEntitiesByIdTelegramAAndCity(Long idTelegram, String name);
}
