package ua.shpp.eqbot.internationalization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.shpp.eqbot.cache.BotUserCache;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class BundleLanguage {

    public static String getValue(long userId, String value) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "language",
                new Locale(BotUserCache.findBy(userId).getLanguage(), value)
        );
        return resourceBundle.getString(value);
    }
}