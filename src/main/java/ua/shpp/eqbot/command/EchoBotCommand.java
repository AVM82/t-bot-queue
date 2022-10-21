package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.service.SendBotMessageService;

@Component("nocommandBotCommand")
public class EchoBotCommand implements BotCommand {
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public EchoBotCommand(SendBotMessageService sendBotMessageService) {
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
