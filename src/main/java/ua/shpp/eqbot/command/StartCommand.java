package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.service.SendBotMessageService;

/**
 * Start {@link Command}.
 */
public class StartCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Старт боту");
        return true;
    }
}