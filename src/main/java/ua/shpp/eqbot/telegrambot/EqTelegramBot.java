package ua.shpp.eqbot.telegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.processors.Processor;


@Component
public class EqTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    private Processor processor;

    @Override
    public void onUpdateReceived(Update update) {
        processor.process(update);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
