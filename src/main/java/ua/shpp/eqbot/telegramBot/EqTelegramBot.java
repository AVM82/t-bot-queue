package ua.shpp.eqbot.telegramBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                SendMessage sm = new SendMessage();
                sm.setText("Ви відправили: " + text);
                sm.setChatId(String.valueOf(message.getChatId()));
                try {
                    execute(sm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
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
