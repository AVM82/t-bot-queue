package ua.shpp.eqbot.internationalization;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.service.UserService;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class BundleLanguage {
    private final UserService userService;
    public final List<String> availableLanguages = List.of("uk", "en");
    public static final String DEFAULT_LANGUAGE = "uk";

    public BundleLanguage(UserService userService) {
        this.userService = userService;
    }

    public String getValue(long userTelegramId, String value) {
        UserDto user = userService.getDto(userTelegramId);
        String language = DEFAULT_LANGUAGE;
        if (user != null) {
            language = user.getLanguage();
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle("language", new Locale(language, value));
        return resourceBundle.getString(value);
    }

    public InlineKeyboardButton createButton(long telegramId, String message, String callbackData) {
        return InlineKeyboardButton.builder().text(getValue(telegramId, message)).callbackData(callbackData).build();
    }
}
