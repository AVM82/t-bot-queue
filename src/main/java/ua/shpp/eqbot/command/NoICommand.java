package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.service.SendBotMessageService;

public class NoICommand implements ICommand {
    private final SendBotMessageService sendBotMessageService;

    public NoICommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), message.getText());
        }
        return true;
    }
}
