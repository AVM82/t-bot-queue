package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;

public class UnknownCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    public UnknownCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    public static final String UNKNOWN_MESSAGE = "not_understand";

    @Override
    public boolean execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), bundleLanguage.getValue(update.getMessage().getChatId(), UNKNOWN_MESSAGE));
        return true;
    }
}