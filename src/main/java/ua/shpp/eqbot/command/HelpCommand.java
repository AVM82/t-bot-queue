package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.service.SendBotMessageService;

public class HelpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("✨<b>Доступні команди</b>✨\n\n"

                    + "<b>Почати\\закінчити работу з ботом</b>\n"
                    + "%s - почати работу зі ботом\n"
                    + "%s - видалити всі свої данні з боту\n"
                    + "%s - зупинити работу з ботом\n\n"

                    + "%s - отримати допомогу\n",
            CommandName.START.getCommandName(), CommandName.DELETE_USER.getCommandName(),
            CommandName.STOP.getCommandName(), CommandName.HELP.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
        return true;
    }
}
