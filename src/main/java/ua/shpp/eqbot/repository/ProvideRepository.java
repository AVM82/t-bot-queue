package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.shpp.eqbot.model.ProviderEntity;


@Repository
public interface ProvideRepository extends JpaRepository<ProviderEntity, Long> {
    @Query("select t from ProviderEntity t where t. id_telegram = ?1")
    ProviderEntity findFirstById_telegram(Long id);
    @Query("select t from ProviderEntity t where t. id_telegram = ?1")
    ProviderEntity findById_telegram(Long id);
}
