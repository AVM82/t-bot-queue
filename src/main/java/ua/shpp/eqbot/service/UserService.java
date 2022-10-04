package ua.shpp.eqbot.service;

import org.modelmapper.ModelMapper;
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
import ua.shpp.eqbot.utility.ConverterDTO;
import ua.shpp.eqbot.validation.UserValidateService;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserValidateService userValidateService;
    private final String dtoCacheName = "cacheDto";
    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, UserValidateService userValidateService, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.userValidateService = userValidateService;
        this.cacheManager = cacheManager;
    }

    public UserEntity getEntity(Long telergamId) {
        LOGGER.info("get userEntity by telergamId " + telergamId);
        UserDto dto = getDto(telergamId);
        return dto != null ? ConverterDTO.convertToEntity(dto) : userRepository.findByTelegramId(telergamId);
    }

    public UserEntity saveEntity(UserEntity userEntity) {
        LOGGER.info("save userEntity " + userEntity);
        userValidateService.checkUserCreation(userEntity.getName(), userEntity.getPhone());
        saveDto(userEntity); // additional
        return userRepository.save(userEntity);
    }

    @CachePut(cacheNames = dtoCacheName, key = "#userDto.telegramId")
    public UserDto saveDto(UserDto userDto) {
        LOGGER.info("save userDto " + userDto);
        return userDto;//****
    }

    @Cacheable(cacheNames = dtoCacheName, key = "#id")
    public UserDto getDto(Long id) {
        LOGGER.info("get userDto by id " + id);
        return null;
    }

    @Transactional
    @CacheEvict(cacheNames = dtoCacheName, key = "#id")
    public boolean remove(Long id) {
        LOGGER.info("delete userDto and All entity");
        userRepository.delete(getEntity(id));
        return true;
    }

    /**
     * its need modificate
     */
    private void saveDto(UserEntity userEntity) {
        Cache cache = cacheManager.getCache(dtoCacheName);
        if (cache != null) { // first attempt ?
            Cache.ValueWrapper valueWrapper = cache.get(userEntity.getTelegramId());
            UserDto userDto;
            if (valueWrapper == null) {
                userDto = ConverterDTO.convertToDto(userEntity);
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
