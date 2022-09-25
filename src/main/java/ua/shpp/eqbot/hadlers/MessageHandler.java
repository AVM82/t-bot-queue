package ua.shpp.eqbot.hadlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.messagesender.MessageSender;
import ua.shpp.eqbot.model.PositionMenu;
import ua.shpp.eqbot.model.PositionRegistration;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.RegistrationUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class MessageHandler implements Handler<Message> {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private final RegistrationUser registrationUser;
    private MessageSender messageSender;
    private final Map<Long, UserDto> cache;
    private final CommandHandler commandHandler;
    private final UserRepository repository;

    public MessageHandler(RegistrationUser registrationUser,
                          CommandHandler commandHandler, CallbackQueryHandler callbackQueryHandler, UserRepository repository) {
        this.cache = callbackQueryHandler.getCache();
        this.registrationUser = registrationUser;
        this.commandHandler = commandHandler;
        this.repository = repository;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void choose(Message message) {
        UserDto user = cache.get(message.getChatId());
        UserEntity byId = repository.findFirstById_telegram(message.getChatId());
        log.info("my id {}", message.getChatId());
        log.info("user byId {}", byId);
        if (byId == null || user == null || user.getPositionRegistration() != PositionRegistration.NONE) {
            log.info("i want registration {}", message.getChatId());
            registrationUser.registration(message);
        } else if (message.hasText() || user.getPositionMenu() == PositionMenu.MENU_START) {
            if (message.hasText() && message.isCommand()) {
                commandHandler.checkCommand(message);
            } else if (user.getPositionMenu() == PositionMenu.MENU_START) {
                createStartMenu(message.getChatId());
            }
        }
    }

    private void createStartMenu(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonCreate = new ArrayList<>();
        buttonCreate.add(InlineKeyboardButton.builder()
                .text("Створити послугу")
                .callbackData("create_service")
                .build());
        List<InlineKeyboardButton> buttonSearch = new ArrayList<>();
        buttonCreate.add(InlineKeyboardButton.builder()
                .text("Обрати послугу")
                .callbackData("search_service")
                .build());

        keyboard.add(buttonCreate);
        keyboard.add(buttonSearch);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Оберіть варіант меню");
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        messageSender.sendMessage(sendMessage);
    }
}
