package ua.shpp.eqbot.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.command.RegistrationNewUser;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final String cacheName = "cacheEntity";
    private final String dtoCacheName = "cacheDto";
    private final ModelMapper modelMapper = new ModelMapper();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(cacheNames = cacheName)
    public UserEntity get(Long id) {
        LOGGER.info("get userEntity by id " + id);
        return userRepository.findFirstById_telegram(id);
    }

    @CacheEvict(cacheNames = cacheName, key = "#userEntity.id_telegram")
    public UserEntity save(UserEntity userEntity) {
        LOGGER.info("save userEntity " + userEntity);

        return userRepository.save(userEntity);
    }


    @CachePut(cacheNames = dtoCacheName, key = "#userDto.id_telegram")
//    @CacheEvict(cacheNames = cacheName, key = "#userDto.id_telegram")
    public UserDto saveDto(UserDto userDto) {
        LOGGER.info("save userDto " + userDto);
        return convertToDto(userRepository.save(convertToEntity(userDto)));
    }

    @Cacheable(cacheNames = dtoCacheName, key = "#id")
    public UserDto getDto(Long id) {
        LOGGER.info("get userDto by id " + id);
        return convertToDto(get(id));
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
