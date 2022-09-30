package ua.shpp.eqbot.internationalization;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.cache.BotUserCache;
import ua.shpp.eqbot.model.UserDto;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class BundleLanguage {

    public static String getValue(long userId, String value) {

        UserDto user = BotUserCache.findBy(userId);
        String language;

        if (user == null)
            language = "uk";
        else
            language = user.getLanguage();

        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "language",
                new Locale(language, value)
        );
        return resourceBundle.getString(value);
    }

    //instance of this class is not needed
    private BundleLanguage() {
    }
}