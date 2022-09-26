package ua.shpp.eqbot.command;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.Cache;
import ua.shpp.eqbot.model.PositionMenu;
import ua.shpp.eqbot.model.PositionRegistration;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

@Service
public class RegistrationNewUser implements Command {
    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationNewUser.class);
    private final SendBotMessageService sendBotMessageService;
    private final UserRepository repository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public void setUserCache(Cache<UserDto> userCache) {
        this.userCache = userCache;
    }

    private Cache<UserDto> userCache;

    @Autowired
    public RegistrationNewUser(SendBotMessageService sendBotMessageService, UserRepository repository) {
        this.sendBotMessageService = sendBotMessageService;
        this.repository = repository;
    }


    @Override
    public boolean execute(Update update) {
        long telegram_id = update.getMessage().getChatId();
        UserDto userDto = userCache.findBy(telegram_id);
        if (userDto != null && userDto.getPositionRegistration() == PositionRegistration.DONE) {
            return true;
        } else if (userDto == null) {
            LOGGER.info("user absent into cash user");
            UserEntity userEntity = repository.findFirstById_telegram(telegram_id);
            if (userEntity != null) {
                LOGGER.info("user present into repo");
                userCache.add(convertToDto(userEntity)
                        .setPositionRegistration(PositionRegistration.DONE));
                return true;
            }
        } else {
            LOGGER.info("new user go to registration method");
            return registration(update.getMessage(),userDto);
        }
        return false;
    }

    private UserDto generateUserFromMessage(Message message) {
        UserDto user = new UserDto();
        user.setName(message.getFrom().getUserName())
                .setId_telegram(message.getChatId())
                .setPositionRegistration(PositionRegistration.INPUT_USERNAME)
                .setPositionMenu(PositionMenu.MENU_START);
        return user;
    }

    private SendMessage createQuery(Long chatId, String text) {
        return SendMessage.builder()
                .text(text)
                .chatId(String.valueOf(chatId))
                .build();
    }


    private boolean registration(Message message, UserDto userDto) {
        LOGGER.info("i try register new user");
        boolean isRegistration =false;
        if (userDto == null) {
            LOGGER.info("new user start registration");
            userCache.add(generateUserFromMessage(message));
            sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                    "Потрібна реєстрація\nвведіть ім'я"));
        } else {
            switch (userDto.getPositionRegistration()) {
                case INPUT_USERNAME:
                    LOGGER.info("new user phase INPUT_USERNAME with message text {}", message.getText());
                    userDto.setName(message.getText());
                    userDto.setPositionRegistration(PositionRegistration.INPUT_CITY);
                    sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                            "Введіть назву вашого міста"));
                    break;
                case INPUT_CITY:
                    LOGGER.info("new user phase INPUT_CITY with message text {}", message.getText());
                    userDto.setCity(message.getText());
                    userDto.setPositionRegistration(PositionRegistration.INPUT_PHONE);
                    sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                            "Введіть номер телефону для зв'язку"));
                    break;
                case INPUT_PHONE:
                    LOGGER.info("new user phase INPUT_PHONE with message text {}", message.getText());
                    userDto.setPhone(message.getText());
                    userDto.setPositionRegistration(PositionRegistration.DONE);
                    UserEntity userEntity = convertToEntity(userDto);
                    repository.save(userEntity);
                    LOGGER.info("save entity to database {}", userEntity);
                    isRegistration= true;
                    sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                            "Дякуємо! Ви зареєстровані" +
                                    "\nid " + userDto.getId_telegram() +
                                    "\nім'я " + userDto.getName() +
                                    "\nмісто " + userDto.getCity() +
                                    "\nтел. " + userDto.getPhone()));
                    break;
            }
        }
        return isRegistration;
    }

    private UserEntity convertToEntity(UserDto userDto) {
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        LOGGER.info("convert dto to entity");
        return userEntity;
    }

    private UserDto convertToDto(UserEntity userEntity) {
        UserDto postDto = modelMapper.map(userEntity, UserDto.class);
        LOGGER.info("convert entity to dto");
        return postDto;
    }
}
