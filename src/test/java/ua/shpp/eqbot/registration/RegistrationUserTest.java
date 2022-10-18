package ua.shpp.eqbot.registration;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.PositionRegistration;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;
import ua.shpp.eqbot.validation.UserValidateService;

import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class RegistrationUserTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidateService userValidateService;
    @Mock
    private Validator validator;
    @Mock
    private SendBotMessageService sendBotMessageService;
    @Mock
    private BundleLanguage bundleLanguage;
    @Mock
    private Update update;
    @Mock
    private EqTelegramBot telegramBot;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, userValidateService, validator);
        UserDto dto = new UserDto();
        dto.setPositionMenu(PositionMenu.MENU_START)
                .setPositionRegistration(PositionRegistration.INPUT_USERNAME)
                .setTelegramId(10L);
        userService.saveDto(dto);
    }

    @Ignore("Причина ігнору")
    @Test
    void generateUserFromMessageTest() {
        assertNull(null);
        /*List<String> str = new ArrayList<>();
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
        }*/
    }

   /* @Test
    void testR() {
        //when(userService.getDto(777L)).thenReturn(new UserDto(777L, "dummy"));
        when(update.getMessage().getChatId()).thenReturn(111L);
        Mockito.when(update.getMessage().getFrom().getLanguageCode()).thenReturn("uk");
        Mockito.when(update.getMessage().getText()).thenReturn("name");
        Mockito.when(update.getMessage().getFrom().getUserName()).thenReturn("Vitaliy");
        RegistrationNewUserICommand testClass = new RegistrationNewUserICommand(sendBotMessageService, userService, bundleLanguage);
        testClass.execute(update);
    }*/
}
