package ua.shpp.eqbot.mapper;

import org.junit.Test;
import ua.shpp.eqbot.dto.UserDto;
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
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getName()).isEqualTo("Oleksandr");
        assertThat(userEntity.getCity()).isEqualTo("Kiev");
    }

    @Test
    public void shouldUserEntityToUserDTO() {
        //given
        UserEntity userEntity = new UserEntity("Oleksandr", "777");

        //when
        UserDto userDto = UserMapper.INSTANCE.userEntityToUserDTO(userEntity);

        //then
        assertThat(userDto).isNotNull();
        assertThat(userDto.getName()).isEqualTo("Oleksandr");
        assertThat(userDto.getPhone()).isEqualTo("777");
    }
}
