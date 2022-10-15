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

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RegistrationUserTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidateService userValidateService;
    @Mock
    private Validator validator;

    @BeforeEach
    public void setUp() {

        userService = new UserService(userRepository, userValidateService, validator);

        UserDto dto = new UserDto();
        dto.setPositionMenu(PositionMenu.MENU_START)
                .setPositionRegistration(PositionRegistration.INPUT_USERNAME)
                .setTelegramId(10L);
        userService.saveDto(dto);
    }

    @Test
    void generateUserFromMessageTest() {
        List<String> str = new ArrayList<>();
        str.add("17:00");
        str.add("city");
        str.add("5");
        str.add("55");
        str.add("555");
        str.add("5555");
        str.add("08.09");
        str.add("01:25");
        for (int i = 0; i < str.size(); ++i) {
            if (str.get(i).matches("\\d+:?.?\\d*")) {
                System.out.println("true " + str.get(i));
            } else System.out.println("false " + str.get(i));
        }
    }
}
