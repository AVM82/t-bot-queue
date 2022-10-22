package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Start {@link BotCommand}.
 */
@Component
public class StartBotCommand implements BotCommand {

    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public StartBotCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        if (update.hasCallbackQuery()) {
            createStartMenu(update.getCallbackQuery().getFrom().getId());
        } else {
            createStartMenu(update.getMessage().getChatId());
        }
        return true;
    }

    private void createStartMenu(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> button = new ArrayList<>();
        createButton(chatId, "create_service", "create_service", button);
        createButton(chatId, "search_service", "search_service", button);
        keyboard.add(button);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(bundleLanguage.getValue(chatId, "choose_menu_option"));
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
    }

    private void createButton(Long id, String nameButton,
                              String dataButton, List<InlineKeyboardButton> button) {
        button.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(id, nameButton))
                .callbackData(dataButton)
                .build());
    }
}
