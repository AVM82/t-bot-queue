package ua.shpp.eqbot.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.PositionRegistration;
import ua.shpp.eqbot.validation.UserValidateService;

@ExtendWith(MockitoExtension.class)
class RegistrationUserTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidateService userValidateService;

    @BeforeEach
    public void setUp() {

        userService = new UserService(userRepository, userValidateService);

        UserDto dto = new UserDto();
        dto.setPositionMenu(PositionMenu.MENU_START)
                .setPositionRegistration(PositionRegistration.INPUT_USERNAME)
                .setTelegramId(10L);
        userService.saveDto(dto);
    }

    @Test
    void generateUserFromMessageTest() {

    }
}
