package ua.shpp.eqbot.utility;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.model.UserEntity;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "language", target = "language")
    UserDto userEntityToUserDTO(UserEntity userEntity);
    @Mapping(source = "language", target = "language")
    UserEntity userDTOToUserEntity(UserDto userDto);
}
