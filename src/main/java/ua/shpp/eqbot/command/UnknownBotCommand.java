package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.stage.icon.Icon;

@Component
public class UnknownBotCommand implements BotCommand {
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public UnknownBotCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    public static final String UNKNOWN_MESSAGE = "not_understand";

    @Override
    public boolean execute(Update update) {
        sendBotMessageService.sendMessage(
                update.getMessage().getChatId().toString(),
                Icon.SHRUG.get() + bundleLanguage.getValue(update.getMessage().getChatId(), UNKNOWN_MESSAGE));
        return true;
    }
}
