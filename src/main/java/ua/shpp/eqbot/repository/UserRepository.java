package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.shpp.eqbot.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select t from UserEntity t where t. idTelegram = ?1")
    UserEntity findFirstByIdTelegram(Long id);
}
