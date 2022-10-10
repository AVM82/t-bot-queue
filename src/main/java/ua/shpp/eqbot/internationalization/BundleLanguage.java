package ua.shpp.eqbot.internationalization;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.service.UserService;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class BundleLanguage {
    private final UserService userService;

    public BundleLanguage(UserService userService) {
        this.userService = userService;
    }

    public String getValue(long userTelegramId, String value) {
        UserDto user = userService.getDto(userTelegramId);
        String language = "uk";
        if (user != null) {
            language = user.getLanguage();
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle("language", new Locale(language, value));
        return resourceBundle.getString(value);
    }
}
