package ua.shpp.eqbot.telegrambot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.telegrambot.service.SendBotMessageService;

/**
 * Start {@link Command}.
 */
public class StartCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    // Тут не добавляємо сервіс через отримання з Application Context.
    // Якщо це зробити так, то буде циклічна залежність, яка поламає роботу додатку.
    // P.S.  spring.main.allow-circular-references=true не дуже хороша ідея...
    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Привет народ!");
    }
}