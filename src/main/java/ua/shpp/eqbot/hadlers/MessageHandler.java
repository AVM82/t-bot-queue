package ua.shpp.eqbot.hadlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.cache.Cache;
import ua.shpp.eqbot.entity.PositionMenu;
import ua.shpp.eqbot.entity.PositionRegistration;
import ua.shpp.eqbot.entity.UserDto;
import ua.shpp.eqbot.messagesender.MessageSender;
import ua.shpp.eqbot.service.RegistrationUser;

import java.util.*;


@Component
public class MessageHandler implements Handler<Message> {
    private RegistrationUser registrationUser;
    private MessageSender messageSender;
    private final Cache<UserDto> cache;
    private CommandHandler commandHandler;

    public MessageHandler(Cache<UserDto> cache, RegistrationUser registrationUser,
                          CommandHandler commandHandler) {
        this.cache = cache;
        this.registrationUser = registrationUser;
        this.commandHandler = commandHandler;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void choose(Message message) {
        UserDto user = cache.findBy(message.getChatId());
        if (user == null || user.getPositionRegistration() != PositionRegistration.NONE) {
            registrationUser.registration(message);
        } else if (message.hasText() || user.getPositionMenu() == PositionMenu.MENU_START) {
            if (message.hasText() && message.isCommand()) {
                commandHandler.checkCommand(message);
            }else if(user.getPositionMenu() == PositionMenu.MENU_START){
                createStartMenu(message.getChatId());
            }
        }
    }

    private void createStartMenu(Long chatId){
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
