package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;

public class SettingsCommand implements Command{

    private final SendBotMessageService sendBotMessageService;

    public static final String SETTINGS_MESSAGE = "Settings.\n";

    public SettingsCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {
        var markup = new ReplyKeyboardMarkup();
        var keyboardRows = new ArrayList<KeyboardRow>();
        KeyboardRow changeUserRole = new KeyboardRow();
        changeUserRole.add("Change role to Provider");
        keyboardRows.add(changeUserRole);
        KeyboardRow changeLang = new KeyboardRow();
        changeUserRole.add("Change lang");
        keyboardRows.add(changeLang);
        KeyboardRow changeCity = new KeyboardRow();
        changeUserRole.add("Change city");
        keyboardRows.add(changeCity);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);
        sendBotMessageService.setReplyMarkup(update.getMessage().getChatId().toString(), markup);
        return true;
    }
}
