package eqbot.mapper;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.mapper.UserMapper;
import ua.shpp.eqbot.model.UserEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserMapperTest {
    @Test
    public void shouldUserDtoToUserEntity() {
        //given
        UserDto userDto = new UserDto("Oleksandr", "Kiev");

        //when
        UserEntity userEntity = UserMapper.INSTANCE.userDTOToUserEntity(userDto);

        //then
        AssertionsForClassTypes.assertThat(userEntity).isNotNull();
        AssertionsForClassTypes.assertThat(userEntity.getName()).isEqualTo("Oleksandr");
        AssertionsForClassTypes.assertThat(userEntity.getCity()).isEqualTo("Kiev");
    }

    @Test
    public void shouldUserEntityToUserDTO() {
        //given
        UserEntity userEntity = new UserEntity("Oleksandr", "777");

        //when
        UserDto userDto = UserMapper.INSTANCE.userEntityToUserDTO(userEntity);

        //then
        AssertionsForClassTypes.assertThat(userDto).isNotNull();
        AssertionsForClassTypes.assertThat(userDto.getName()).isEqualTo("Oleksandr");
        AssertionsForClassTypes.assertThat(userDto.getPhone()).isEqualTo("777");
    }
}
