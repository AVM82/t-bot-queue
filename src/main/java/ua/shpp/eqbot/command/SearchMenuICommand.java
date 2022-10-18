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
 * Start {@link ICommand}.
 */
public class SearchMenuICommand implements ICommand {

    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    public SearchMenuICommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        if (update.hasCallbackQuery()) {
            createMenu(update.getCallbackQuery().getFrom().getId());
        } else {
            createMenu(update.getMessage().getChatId());
        }
        return true;
    }

    private void createMenu(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonCreate = new ArrayList<>();

        buttonCreate.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(chatId, "search.searchByCityName"))
                .callbackData("searchName")
                .build());
        buttonCreate.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(chatId, "search.searchId"))
                .callbackData("searchId")
                .build());
        buttonCreate.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(chatId, "search.searchString"))
                .callbackData("searchString")
                .build());
        keyboard.add(buttonCreate);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(bundleLanguage.getValue(chatId, "choose_menu_option"));
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
    }
}
