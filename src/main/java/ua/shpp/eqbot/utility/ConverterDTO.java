package ua.shpp.eqbot.utility;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.model.UserEntity;

public class ConverterDTO {
    private static final ModelMapper modeMODEL_MAPPERMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterDTO.class);

    public static UserEntity convertToEntity(UserDto userDto) {
        UserEntity userEntity = modeMODEL_MAPPERMapper.map(userDto, UserEntity.class);
        LOGGER.info("convert dto to entity");
        return userEntity;
    }

    public static UserDto convertToDto(UserEntity userEntity) {
        UserDto postDto = modeMODEL_MAPPERMapper.map(userEntity, UserDto.class);
        LOGGER.info("convert entity to dto");
        return postDto;
    }
}
