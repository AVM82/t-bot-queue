package ua.shpp.eqbot.command;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.model.PositionMenu;
import ua.shpp.eqbot.model.PositionRegistration;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegistrationNewUser implements Command {

    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationNewUser.class);
    private final SendBotMessageService sendBotMessageService;
    private final UserRepository repository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Map<Long, UserDto> dtoHashMap = new HashMap<>();

    @Autowired
    public RegistrationNewUser(SendBotMessageService sendBotMessageService, UserRepository repository) {
        this.sendBotMessageService = sendBotMessageService;
        this.repository = repository;
    }

    /*TODO it's complicated and something is missing here*/
    private UserDto generateUserFromMessage(Message message) {
        UserDto user = new UserDto();
        user.setName(message.getFrom().getUserName())
                .setId_telegram(message.getChatId())
                .setPositionRegistration(PositionRegistration.INPUT_USERNAME)
                .setPositionMenu(PositionMenu.MENU_START);
        return user;
    }

    /*TODO what the .......*/
    @Override
    public boolean execute(Update update) {

        LOGGER.info("i try register new user {}", update.getMessage().getText());
        Message message = update.getMessage();
        UserDto user = dtoHashMap.get(message.getChatId());
        if (repository.findFirstById_telegram(message.getChatId()) != null) {
            LOGGER.info("don't do it");
            return true;
        }
        boolean isRegistration = false;

        if (user == null) {
            LOGGER.info("new user start registration");
            user = generateUserFromMessage(message);
            dtoHashMap.put(user.getId_telegram(), user);
            sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                    "Потрібна реєстрація\nвведіть ім'я"));

        } else {
            switch (user.getPositionRegistration()) {
                case INPUT_USERNAME:
                    LOGGER.info("new user phase INPUT_USERNAME with message text {}", message.getText());
                    user.setName(message.getText());
                    user.setPositionRegistration(PositionRegistration.INPUT_CITY);
                    sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                            "Введіть назву вашого міста"));
                    break;
                case INPUT_CITY:
                    LOGGER.info("new user phase INPUT_CITY with message text {}", message.getText());
                    user.setCity(message.getText());
                    user.setPositionRegistration(PositionRegistration.NONE);
                    UserEntity userEntity = convertToEntity(user);
                    repository.save(userEntity);
                    LOGGER.info("save entity to database {}", userEntity);
                    isRegistration = true;
                    sendBotMessageService.sendMessage(createQuery(message.getChatId(),
                            "Дякуємо! Ви зареєстровані" +
                                    "\nid " + user.getId_telegram() +
                                    "\nім'я " + user.getName() +
                                    "\nмісто " + user.getCity()));
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

    private SendMessage createQuery(Long chatId, String text) {
        return SendMessage.builder()
                .text(text)
                .chatId(String.valueOf(chatId))
                .build();
    }
}
