package ua.shpp.eqbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.shpp.eqbot.dto.PrevPositionDTO;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.mapper.UserMapper;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.validation.UserValidateService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private static final String DTO_CACHE_NAME = "cacheUserDto";
    private final UserValidateService userValidateService;
    private final Validator validator;

    public UserService(UserRepository userRepository, UserValidateService userValidateService, Validator validator) {
        this.userRepository = userRepository;
        this.userValidateService = userValidateService;
        this.validator = validator;
    }

    public UserEntity getEntity(Long telegramId) {
        LOGGER.info("get userEntity by telegramId {}", telegramId);
        UserDto dto = getDto(telegramId);
        return dto != null ? UserMapper.INSTANCE.userDTOToUserEntity(dto) : userRepository.findByTelegramId(telegramId);
    }

    @Cacheable(cacheNames = "prevPosition", key = "#telegramId")
    public PrevPositionDTO getPrevPosition(Long telegramId) {
        return null;
    }

    @CachePut(cacheNames = "prevPosition", key = "#prevPositionDTO.telegramId")
    public PrevPositionDTO putPrevPosition(PrevPositionDTO prevPositionDTO) {
        return prevPositionDTO;
    }


    public UserEntity saveEntity(UserEntity userEntity) {
        LOGGER.info("save userEntity {}", userEntity);
        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);
        if (userRepository.findByTelegramId(userEntity.getTelegramId()) == null
                && (!violations.isEmpty() || !(userValidateService.checkUserCreation(userEntity.getName(), userEntity.getPhone())))) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<UserEntity> constraintViolation : violations) {
                stringBuilder.append(constraintViolation.getMessage());
            }
            LOGGER.info("the model was not inserted into the database because it did not pass validation");
            LOGGER.error("Error occurred: {}, {}", stringBuilder, validator);

            return null;
        }
        LOGGER.info("model save to DB with id {}", userEntity.getTelegramId());
        UserEntity result = userRepository.save(userEntity);
        saveDto(UserMapper.INSTANCE.userEntityToUserDTO(userEntity)); // additional
        LOGGER.info("success created new userEntity");
        return result;
    }

    @CachePut(cacheNames = DTO_CACHE_NAME, key = "#userDto.telegramId")
    public UserDto saveDto(UserDto userDto) {
        LOGGER.info("save userDto {}", userDto);
        return userDto;
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
        if (entity != null) {
            userRepository.delete(getEntity(telegramId));
        }
        return true;
    }

    public void updateUserInDB(@NotNull UserDto dto) {
        LOGGER.info("update user language");
        UserEntity myUser = userRepository.findByTelegramId(dto.getTelegramId());
        UserMapper.INSTANCE.updateUserFromDto(dto, myUser);
        userRepository.save(myUser);
    }
}
