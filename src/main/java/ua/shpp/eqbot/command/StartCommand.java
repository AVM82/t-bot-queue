package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Start {@link Command}.
 */
public class StartCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    public StartCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        if (update.hasCallbackQuery())
            createStartMenu(update.getCallbackQuery().getFrom().getId());
        else
            createStartMenu(update.getMessage().getChatId());
        return true;
    }

    private void createStartMenu(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonCreate = new ArrayList<>();
        buttonCreate.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(chatId, "create_service"))
                .callbackData("create_service")
                .build());
        List<InlineKeyboardButton> buttonSearch = new ArrayList<>();
        buttonCreate.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(chatId, "search_service"))
                .callbackData("search_service")
                .build());
        keyboard.add(buttonCreate);
        keyboard.add(buttonSearch);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(bundleLanguage.getValue(chatId, "choose_menu_option"));
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
    }
}
