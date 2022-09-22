package ua.shpp.eqbot.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    private final static Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            sendingResponse(chatId, update.getMessage().getText());
        }
    }

    private void sendingResponse(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException ex) {
            LOGGER.error("fail execute message {}", message.getText());
        }
    }

    @Override
    public String getBotUsername() {
        return "QueueTBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
