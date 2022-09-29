package ua.shpp.eqbot.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;
    private final String cacheName = "test";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(cacheNames = cacheName)
    public UserEntity getUserByTelegramId(Long id) {
        return userRepository.findFirstById_telegram(id);
    }

    @CacheEvict(cacheNames = cacheName, key = "#userEntity.id")
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
