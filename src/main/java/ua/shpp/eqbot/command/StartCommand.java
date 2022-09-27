package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Start {@link Command}.
 */
public class StartCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {
        createStartMenu(update.getMessage().getChatId());
        //sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Старт боту");
        return true;
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
        sendBotMessageService.sendMessage(sendMessage);
    }
}