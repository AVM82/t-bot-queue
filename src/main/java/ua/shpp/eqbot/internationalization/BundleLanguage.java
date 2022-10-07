package ua.shpp.eqbot.internationalization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.service.UserService;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class BundleLanguage {

    private static final Logger LOGGER = LoggerFactory.getLogger(BundleLanguage.class);

    private final UserService userService;

    public BundleLanguage(UserService userService) {
        this.userService = userService;
    }

    public String getValue(long userTelegramId, String value) {
        LOGGER.info("i try set user language");
        UserEntity user = userService.getEntity(userTelegramId);
        String language = "uk";
        if (user != null) {
            language = user.getLanguage();
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle("language", new Locale(language, value));
        LOGGER.info("user language is {}", language);
        return resourceBundle.getString(value);
    }

    //instance of this class is not needed
}
