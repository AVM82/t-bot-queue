package ua.shpp.eqbot.service;

import com.sun.xml.bind.v2.schemagen.episode.SchemaBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.mapper.UserMapper;
import ua.shpp.eqbot.validation.UserValidateService;

@Service
public class UserService{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final String dtoCacheName = "cacheUserDto";
    private final CacheManager cacheManager;
    private  final UserValidateService userValidateService;


    public UserService(UserRepository userRepository, UserValidateService userValidateService, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.userValidateService = userValidateService;
        this.cacheManager = cacheManager;
    }

    public UserEntity getEntity(Long telegramId) {
        LOGGER.info("get userEntity by telergamId {}", telegramId);
        UserDto dto = getDto(telegramId);
        return dto != null ? UserMapper.INSTANCE.userDTOToUserEntity(dto) : userRepository.findByTelegramId(telegramId);
    }

    public UserEntity saveEntity(UserEntity userEntity) {
        LOGGER.info("save userEntity {}", userEntity);
        saveDto(userEntity); // additional
        return userRepository.save(userEntity);
    }

    @CachePut(cacheNames = dtoCacheName, key = "#userDto.telegramId")
    public UserDto saveDto(UserDto userDto) {
        LOGGER.info("save userDto {}", userDto);
        return userDto;//****
    }

    @Cacheable(cacheNames = dtoCacheName, key = "#id")
    public UserDto getDto(Long id) {
        LOGGER.info("get userDto by id {}", id);
        return null;
    }

    @Transactional
    @CacheEvict(cacheNames = dtoCacheName, key = "#id")
    public boolean remove(Long id) {
        LOGGER.info("delete userDto and All entity");
        UserEntity entity = getEntity(id);
        if (entity != null)
            userRepository.delete(getEntity(id));
        return true;
    }

    /**
     * its need modification
     */
    private void saveDto(UserEntity userEntity) {
        Cache cache = cacheManager.getCache(dtoCacheName);
        if (cache != null) { // first attempt ?
            Cache.ValueWrapper valueWrapper = cache.get(userEntity.getTelegramId());
            UserDto userDto;
            if (valueWrapper == null) {
                userDto = UserMapper.INSTANCE.userEntityToUserDTO(userEntity);
            } else {
                userDto = ((UserDto) valueWrapper.get())
                        .setName(userEntity.getName())
                        .setLanguage(userEntity.getLanguage())
                        .setCity(userEntity.getCity())
                        .setPhone(userEntity.getPhone());
            }
            cache.put(userEntity.getTelegramId(), userDto);
        }
    }
}
