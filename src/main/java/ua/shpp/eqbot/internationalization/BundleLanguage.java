package ua.shpp.eqbot.internationalization;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.cache.BotUserCache;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class BundleLanguage {

    public static String getValue(long userId, String value) {
        String language = BotUserCache.findBy(userId).getLanguage();
        if (language == null)
            language = "uk";
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "language",
                new Locale(language, value)
        );
        return resourceBundle.getString(value);
    }

    //instance of this class is not needed
    private BundleLanguage() {}
}