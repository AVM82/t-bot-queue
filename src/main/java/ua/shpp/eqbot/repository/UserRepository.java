package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.shpp.eqbot.model.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select t from UserEntity t where t. id_telegram = ?1")
    UserEntity findFirstById_telegram(Long id);
}
