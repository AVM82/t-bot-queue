package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

@Component
public class ChangeLanguageBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeLanguageBotCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public ChangeLanguageBotCommand(SendBotMessageService sendBotMessageService, UserService userService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        long id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else {
            id = update.getMessage().getChatId();
        }

        UserDto userDto = userService.getDto(id);

        if (userDto == null) {
            LOGGER.info("User not found");
            return false;
        }

        if (!userDto.getLanguage().equals("en")) {
            userDto.setLanguage("en");
        } else {
            userDto.setLanguage("uk");
        }

        userService.saveDto(userDto);
        userService.updateUserInDB(userDto);

        LOGGER.info("The interface language has been changed");
        sendBotMessageService.sendMessage(String.valueOf(id), bundleLanguage.getValue(id, "change_language_success"));

        return true;
    }
}
