//package ua.shpp.eqbot.command;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.telegram.telegrambots.meta.api.objects.Chat;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import ua.shpp.eqbot.dto.UserDto;
//import ua.shpp.eqbot.internationalization.BundleLanguage;
//import ua.shpp.eqbot.mapper.UserMapper;
//import ua.shpp.eqbot.model.UserEntity;
//import ua.shpp.eqbot.repository.ServiceRepository;
//import ua.shpp.eqbot.repository.UserRepository;
//import ua.shpp.eqbot.service.SendBotMessageService;
//import ua.shpp.eqbot.service.UserService;
//import ua.shpp.eqbot.validation.UserValidateService;
//
//import javax.validation.Validator;
//
//import static org.mockito.Mockito.when;
//
//class SearchUsesNameServiceTest {
//    @Mock
//    SendBotMessageService sendBotMessageService;
//    @Mock
//    ServiceRepository serviceRepository;
//    private UserService userService;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private UserValidateService userValidateService;
//    @Mock
//    private Validator validator;
//    @Mock
//    BundleLanguage bundleLanguage;
//    private SearchUsesNameService search;
//
//    @BeforeEach
//    public void setUp() {
//        userService = new UserService(userRepository, userValidateService, validator);
//        search = new SearchUsesNameService(sendBotMessageService, serviceRepository,
//                userService, bundleLanguage);
//        UserDto userDto = new UserDto();
//        userDto.setName("Oleksandr");
//        userDto.setTelegramId(777L);
//        UserEntity userEntity = UserMapper.INSTANCE.userDTOToUserEntity(userDto);
//        userService.saveEntity(userEntity);
//    }
//
//    @Test
//    void whenSearchThenResult() {
//        Update update = new Update();
//        Message message = new Message();
//        message.setText("роб");
//        Chat chat = new Chat();
//        chat.setId(777L);
//        message.setChat(chat);
//        update.setMessage(message);
////        when(userService.getDto(777L)).thenReturn(new UserDto(777L, "dummy"));
//        search.execute(update);
//    }
//
//}