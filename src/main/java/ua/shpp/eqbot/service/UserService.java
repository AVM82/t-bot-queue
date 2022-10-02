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
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final String dtoCacheName = "cacheUserDto";
    private final CacheManager cacheManager;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserService(UserRepository userRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

    public UserEntity getEntity(Long id) {
        LOGGER.info("get userEntity by id {}", id);
        UserDto dto = getDto(id);
        return dto != null ? convertToEntity(dto) : userRepository.findFirstByIdTelegram(id);
    }

    public UserEntity saveEntity(UserEntity userEntity) {
        LOGGER.info("save userEntity {}", userEntity);
        saveDto(userEntity); // additional
        return userRepository.save(userEntity);
    }

    @CachePut(cacheNames = dtoCacheName, key = "#userDto.idTelegram")
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
        userRepository.delete(getEntity(id));
        return true;
    }

    /**
     * its need modification
     */
    private void saveDto(UserEntity userEntity) {
        Cache cache = cacheManager.getCache(dtoCacheName);
        if (cache != null) { // first attempt ?
            Cache.ValueWrapper valueWrapper = cache.get(userEntity.getIdTelegram());
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
            cache.put(userEntity.getIdTelegram(), userDto);
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
