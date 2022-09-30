package ua.shpp.eqbot.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;

import java.util.Objects;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    private final String dtoCacheName = "cacheDto";
    private final ModelMapper modelMapper = new ModelMapper();

    public UserService(UserRepository userRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

    public UserEntity getEntity(Long id) {
        LOGGER.info("get userEntity by id " + id);
        return userRepository.findFirstById_telegram(id);
    }

    public UserEntity saveEntity(UserEntity userEntity) {
        LOGGER.info("save userEntity " + userEntity);
        saveDto(userEntity); // additional
        return userRepository.save(userEntity);
    }

    @CachePut(cacheNames = dtoCacheName, key = "#userDto.id_telegram")
    public UserDto saveDto(UserDto userDto) {
        LOGGER.info("save userDto " + userDto);
        return convertToDto(userRepository.save(convertToEntity(userDto)));
    }

    @Cacheable(cacheNames = dtoCacheName, key = "#id")
    public UserDto getDto(Long id) {
        LOGGER.info("get userDto by id " + id);
        return convertToDto(get(id));
    }

    /**
     * its need modificate
     */
    private void saveDto(UserEntity userEntity) {
        Cache cache = cacheManager.getCache(dtoCacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(userEntity.getId());
            UserDto userDto;
            if (valueWrapper == null) {
                userDto = convertToDto(userEntity);
            } else {
                userDto = ((UserDto) valueWrapper.get())
                        .setName(userEntity.getName())
                        .setLanguage(userEntity.getLanguage())
                        .setCity(userEntity.getCity())
                        .setPhone(userEntity.getPhone());
            }
            cache.put(userEntity.getId(), userDto);
        }
    }

    private UserEntity convertToEntity(UserDto userDto) {
        if (userDto == null) return null;
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        LOGGER.info("convert dto to entity");
        return userEntity;
    }

    private UserDto convertToDto(UserEntity userEntity) {
        if (userEntity == null) return null;
        UserDto postDto = modelMapper.map(userEntity, UserDto.class);
        LOGGER.info("convert entity to dto");
        return postDto;
    }
}
