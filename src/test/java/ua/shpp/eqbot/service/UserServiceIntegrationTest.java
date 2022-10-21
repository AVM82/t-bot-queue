package ua.shpp.eqbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.telegram.telegrambots.starter.TelegramBotInitializer;
import ua.shpp.eqbot.command.CommandContainer;
import ua.shpp.eqbot.command.registrationservice.AddServiceBotCommand;
import ua.shpp.eqbot.config.SecurityConfiguration;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.mapper.UserMapper;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;
import ua.shpp.eqbot.validation.UserValidateService;

import javax.validation.Validator;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIntegrationTest {
    @MockBean
    AddServiceBotCommand registrationServiceCommand;
    @MockBean
    CommandContainer commandContainer;
    @MockBean
    ImageService imageService;
    @MockBean
    TelegramBotInitializer telegramBotInitializer;
    @MockBean
    EqTelegramBot eqTelegramBot;
    @MockBean
    ProviderService providerService;
    @MockBean
    ProviderRepository providerRepository;
    @MockBean
    Validator validator;
    @MockBean
    SecurityConfiguration securityConfiguration;
    @MockBean
    SecurityFilterChain securityFilterChain;
    @Autowired
    private UserValidateService userValidateService;

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userValidateService, validator);
    }

    @Test
    void getEntity() {
        UserDto userDto = new UserDto();
        userDto.setName("kolobok");
        userDto.setTelegramId(1L);
        UserEntity userEntity = UserMapper.INSTANCE.userDTOToUserEntity(userDto);
        userService.saveEntity(userEntity);
        UserEntity dto = userService.getEntity(1L);

        assertThat(dto.getName(), is("kolobok"));
    }

    @Test
    void whenRemoveThanEmptyResult() {
        UserEntity entity = new UserEntity();
        entity.setName("Did Moroz");
        entity.setTelegramId(1L);

        userService.saveEntity(entity);

        UserEntity dto = userService.getEntity(1L);

        assertThat(dto.getName(), is("Did Moroz"));
        userService.remove(1L);

        assertNull(userService.getEntity(1L));
    }

    @Test
    void shouldCreateOneUser() {
        final var name = "Oleksandr";
        final var phone = "777";
        UserEntity userEntity = new UserEntity();
        userEntity.setTelegramId(7L).setName(name).setPhone(phone);

        final var user = userRepository.save(userEntity);

        assertEquals(7L, user.getTelegramId());
        assertEquals(name, user.getName());
        assertEquals(phone, user.getPhone());
        assertTrue(user.getCreatedTime().isBefore(LocalDateTime.now()));
    }
}
