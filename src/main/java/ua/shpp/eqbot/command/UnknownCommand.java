package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.service.SendBotMessageService;

public class UnknownCommand implements Command {
    private final SendBotMessageService sendBotMessageService;

    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    public static final String UNKNOWN_MESSAGE = "Не розумію вас \uD83D\uDE1F, напишіть /help для отримання допомоги.";

    @Override
    public boolean execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), UNKNOWN_MESSAGE);
        return true;
    }
}