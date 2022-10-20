package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.service.SendBotMessageService;

@Component
public class HelpBotCommand implements BotCommand {

    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("✨<b>Доступні команди</b>✨%n%n"
                    + "<b>Почати\\закінчити работу з ботом</b>%n"
                    + "%s - почати работу зі ботом%n"
                    + "%s - видалити всі свої данні з боту%n"
                    + "%s - зупинити работу з ботом%n%n"
                    + "%s - отримати допомогу%n",
            CommandName.START.getNameCommand(), CommandName.DELETE_USER.getNameCommand(),
            CommandName.STOP.getNameCommand(), CommandName.HELP.getNameCommand());

    @Autowired
    public HelpBotCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
        return true;
    }
}
