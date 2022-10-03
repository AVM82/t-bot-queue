package ua.shpp.eqbot.command;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.PositionRegistration;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.PositionMenu;
import ua.shpp.eqbot.model.PositionRegistration;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

@Component
public class RegistrationNewUser implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationNewUser.class);
    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public RegistrationNewUser(SendBotMessageService sendBotMessageService, UserService userService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
    }

    /**
     * registration check in cache and database
     *
     * @param update provided {@link Update} object with all the needed data for command.
     * @return - true if registration passed
     */
    @Override
    public boolean execute(Update update) {
        UserDto userDto = userService.getDto(update.getMessage().getChatId());
        if (userDto == null) {
            LOGGER.info("user absent into cash user");
            UserEntity userEntity = userService.getEntity(update.getMessage().getChatId());
            if (userEntity != null) {
                LOGGER.info("user present into repo");
                userService.saveDto(convertToDto(userEntity).setPositionRegistration(PositionRegistration.DONE));
                return true;
            }
            return registration(update.getMessage(), null);
        } else if (userDto.getPositionRegistration() == PositionRegistration.DONE) {
            return true;
        } else {
            LOGGER.info("new user go to registration method");
            return registration(update.getMessage(), userDto);
        }
    }

    /**
     * creating a user from the data from the message
     *
     * @param message - message from user
     * @return - initial user data
     */
    private UserDto generateUserFromMessage(Message message) {
        UserDto user = new UserDto();
        user.setName(message.getFrom().getUserName())
                .setTelegramId(message.getChatId())
                .setPositionRegistration(PositionRegistration.INPUT_USERNAME)
                .setLanguage(message.getFrom().getLanguageCode())
                .setPositionMenu(PositionMenu.MENU_START);
        return user;
    }

    private SendMessage createQuery(Long chatId, String text) {
        return SendMessage.builder()
                .text(text)
                .chatId(String.valueOf(chatId))
                .build();
    }

    /**
     * User registration
     *
     * @param message - message from user
     * @param userDto - user data to save to the database
     * @return - true if registration is complete
     */
    private boolean registration(Message message, UserDto userDto) {
        LOGGER.info("i try register new user");
        boolean isRegistration = false;
        if (userDto == null) {
            LOGGER.info("new user start registration");
            userService.saveDto(generateUserFromMessage(message));
            sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                    bundleLanguage.getValue(message.getChatId(), "input_name")));
        } else {
            LOGGER.info("i want to next stage registration {}", userDto);
            switch (userDto.getPositionRegistration()) {
                case INPUT_USERNAME:
                    LOGGER.info("new user phase INPUT_USERNAME with message text {}", message.getText());
                    if(message.isCommand()) break;
                    userDto.setName(message.getText());
                    userDto.setPositionRegistration(PositionRegistration.INPUT_CITY);
                    sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                            bundleLanguage.getValue(message.getChatId(), "input_city")));
                    break;
                case INPUT_CITY:
                    LOGGER.info("new user phase INPUT_CITY with message text {}", message.getText());
                    if(message.isCommand()) break;
                    userDto.setCity(message.getText());
                    userDto.setPositionRegistration(PositionRegistration.INPUT_PHONE);
                    sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                            bundleLanguage.getValue(message.getChatId(), "input_phone_number")));
                    break;
                case INPUT_PHONE:
                    LOGGER.info("new user phase INPUT_PHONE with message text {}", message.getText());
                    if(message.isCommand()) break;
                    userDto.setPhone(message.getText());
                    userDto.setPositionRegistration(PositionRegistration.DONE);
                    UserEntity userEntity = convertToEntity(userDto);
                    userService.saveEntity(userEntity);
                    LOGGER.info("save entity to database {}", userEntity);
                    isRegistration = true;
                    sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                            String.format(bundleLanguage.getValue(
                                            message.getChatId(), "registered"),
                                    userDto.getTelegramId(), userDto.getName(), userDto.getCity(), userDto.getPhone())));
                    break;
                default:
                    //do nothing
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
