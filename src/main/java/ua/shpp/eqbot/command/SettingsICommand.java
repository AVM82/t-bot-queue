package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;

public class SettingsICommand implements ICommand {

    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    public SettingsICommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        var markup = new ReplyKeyboardMarkup();
        var keyboardRows = new ArrayList<KeyboardRow>();
        KeyboardRow changeUserRole = new KeyboardRow();
        changeUserRole.add(bundleLanguage.getValue(update.getMessage().getChatId(), "change_role_to_provider"));
        keyboardRows.add(changeUserRole);
        KeyboardRow changeLang = new KeyboardRow();
        changeUserRole.add(bundleLanguage.getValue(update.getMessage().getChatId(), "change_language"));
        keyboardRows.add(changeLang);
        KeyboardRow changeCity = new KeyboardRow();
        changeUserRole.add(bundleLanguage.getValue(update.getMessage().getChatId(), "change_city"));
        keyboardRows.add(changeCity);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);
        sendBotMessageService.setReplyMarkup(update.getMessage().getChatId().toString(), markup);
        return true;
    }
}
