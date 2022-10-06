package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shpp.eqbot.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByTelegramId(Long telegramId);

    void deleteAllByTelegramId(Long telegramId);

    boolean existsByNameAndPhone(String name, String phone);
}
