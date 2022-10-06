package ua.shpp.eqbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.mapper.UserMapper;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.validation.UserValidateService;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private static final String DTO_CACHE_NAME = "cacheUserDto";


    public UserService(UserRepository userRepository, UserValidateService userValidateService) {
        this.userRepository = userRepository;
    }

    @Cacheable(cacheNames = "cacheEntity", key = "#telegramId")
    public UserEntity getEntity(Long telegramId) {
        LOGGER.info("get userEntity by telegramId {}", telegramId);
        UserDto dto = getDto(telegramId);
        return dto != null ? UserMapper.INSTANCE.userDTOToUserEntity(dto) : userRepository.findByTelegramId(telegramId);
    }

    @CachePut(cacheNames = "cacheEntity")
    public UserEntity saveEntity(UserEntity userEntity) {
        LOGGER.info("save userEntity {}", userEntity);
        saveDto(UserMapper.INSTANCE.userEntityToUserDTO(userEntity)); // additional
        return userRepository.save(userEntity);
    }

    @CachePut(cacheNames = DTO_CACHE_NAME, key = "#userDto.telegramId")
    public UserDto saveDto(UserDto userDto) {
        LOGGER.info("save userDto {}", userDto);
        return userDto;//****
    }

    @Cacheable(cacheNames = DTO_CACHE_NAME, key = "#telegramId")
    public UserDto getDto(Long telegramId) {
        LOGGER.info("get userDto by telegramId {}", telegramId);
        return null;
    }

    @Transactional
    @CacheEvict(cacheNames = {DTO_CACHE_NAME, "cacheEntity"}, key = "#telegramId")
    public boolean remove(Long telegramId) {
        LOGGER.info("delete userDto and All entity");
        UserEntity entity = getEntity(telegramId);
        if (entity != null) userRepository.delete(getEntity(telegramId));
        return true;
    }
}

