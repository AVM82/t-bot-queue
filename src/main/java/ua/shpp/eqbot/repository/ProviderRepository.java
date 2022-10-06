package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.shpp.eqbot.model.ProviderEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    @Query("select t from ProviderEntity t where t. telegramId = ?1")
    ProviderEntity findFirstByTelegramId(Long id);

    @Query("select t from ProviderEntity t where t. telegramId = ?1")
    ProviderEntity findByTelegramId(Long id);

    @Query("select t from ProviderEntity t where t. telegramId = ?1")
    List<ProviderEntity> findAllByTelegramId(Long id);

    Optional<ProviderEntity> findProviderEntitiesByTelegramIdAndProviderCity(Long telegramId, String name);

    List<ProviderEntity> findAllByProviderCity(String city);

    ProviderEntity findFirstById(Long id);

}
