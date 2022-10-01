package ua.shpp.eqbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.telegram.telegrambots.starter.TelegramBotInitializer;
import ua.shpp.eqbot.command.AddService;
import ua.shpp.eqbot.command.CommandContainer;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class UserServiceTest {
    @MockBean
    AddService addService;
    @MockBean
    CommandContainer commandContainer;
    @MockBean
    ImageService imageService;
    @MockBean
    TelegramBotInitializer telegramBotInitializer;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, cacheManager);
    }

    private UserEntity convertToEntity(UserDto userDto) {
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        return userEntity;
    }

    @Test
    void getEntity() {
        UserDto userDto = new UserDto();
        userDto.setName("kolobok");
        userDto.setIdTelegram(1L);
        UserEntity userEntity = convertToEntity(userDto);
        userService.saveEntity(userEntity);
        UserEntity dto = userService.getEntity(1L);

        assertThat(dto.getName(), is("kolobok"));
    }

    @Test
    void whenRemoveThanEmptyResult() {
        UserEntity entity = new UserEntity();
        entity.setName("Did Moroz");
        entity.setIdTelegram(1L);

        userService.saveEntity(entity);

        UserEntity dto = userService.getEntity(1L);

        assertThat(dto.getName(), is("Did Moroz"));
        userService.remove(1L);

        assertNull(userService.getEntity(1L));
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