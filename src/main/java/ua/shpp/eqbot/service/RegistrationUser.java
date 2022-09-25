package ua.shpp.eqbot.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.shpp.eqbot.hadlers.CallbackQueryHandler;
import ua.shpp.eqbot.messagesender.MessageSender;
import ua.shpp.eqbot.model.PositionMenu;
import ua.shpp.eqbot.model.PositionRegistration;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/* TODO it is possible to store temporary user data during registration*/
@Service
public class RegistrationUser {
    private static final Logger log = LoggerFactory.getLogger(RegistrationUser.class);

    private final UserRepository repository;
    private final MessageSender messageSender;
    private final ModelMapper modelMapper = new ModelMapper();
    private final CallbackQueryHandler callbackQueryHandler;
    private final Map<Long, UserDto> dtoHashMap;

    @Autowired
    public RegistrationUser(UserRepository repository, MessageSender messageSender, CallbackQueryHandler callbackQueryHandler) {
        this.repository = repository;
        this.messageSender = messageSender;
        this.callbackQueryHandler = callbackQueryHandler;
        dtoHashMap = callbackQueryHandler.getCache();
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

    /*TODO what the fuck*/
    public boolean registration(Message message) {
        boolean isRegistration = false;
        UserDto user = dtoHashMap.get(message.getChatId());
        if (user == null) {
            log.info("new user start registration");
            user = generateUserFromMessage(message);
            callbackQueryHandler.getCache().put(user.getId_telegram(), user);
            messageSender.sendMessage(createQuery(message.getChatId(),
                    "Потрібна реєстрація\nвведіть ім'я"));
        } else {
            switch (user.getPositionRegistration()) {
                case INPUT_USERNAME:
                    user.setName(message.getText());
                    user.setPositionRegistration(PositionRegistration.INPUT_CITY);
                    messageSender.sendMessage(createQuery(message.getChatId(),
                            "Введіть назву вашого міста"));
                    break;
                case INPUT_CITY:
                    user.setCity(message.getText());
                    user.setPositionRegistration(PositionRegistration.NONE);
                    UserEntity userEntity = convertToEntity(user);
                    repository.save(userEntity);
                    log.info("save entity to database {}", userEntity);
                    isRegistration = true;
                    messageSender.sendMessage(createQuery(message.getChatId(),
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
        log.info("convert dto to entity");
        return userEntity;
    }

    private UserDto convertToDto(UserEntity userEntity) {
        UserDto postDto = modelMapper.map(userEntity, UserDto.class);
        log.info("convert entity to dto");
        return postDto;
    }

    private SendMessage createQuery(Long chatId, String text) {
        return SendMessage.builder()
                .text(text)
                .chatId(String.valueOf(chatId))
                .build();
    }
}
