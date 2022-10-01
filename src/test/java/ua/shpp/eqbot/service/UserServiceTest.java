package ua.shpp.eqbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import ua.shpp.eqbot.model.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private CacheManager cacheManager;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void getEntity() {
        UserDto userDto = new UserDto();
        userDto.setName("kolobok");
        userDto.setIdTelegram(1L);
        userService.saveDto(userDto);
        UserDto dto = userService.getDto(1L);
        assertThat(dto.getName(), is("kolobok"));

    }

    @Test
    void saveEntity() {
        UserDto userDto = new UserDto();
        userDto.setName("kolobok");
        userService.saveDto(userDto);
    }

    @Test
    void saveDto() {
    }

    @Test
    void getDto() {
    }

    @Test
    void remove() {
    }
}