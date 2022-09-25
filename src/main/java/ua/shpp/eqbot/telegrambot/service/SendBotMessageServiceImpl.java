package ua.shpp.eqbot.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    Logger log = LoggerFactory.getLogger(SendBotMessageServiceImpl.class);

    private final EqTelegramBot telegramBot;

    @Autowired
    public SendBotMessageServiceImpl(EqTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn(e.getLocalizedMessage());
        }
    }

    @Override
    public void sendMessage(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.warn(e.getLocalizedMessage());
        }
    }

    @Override
    public void setReplyMarkup(String chatId, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText("Choose command");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn(e.getLocalizedMessage());
        }
    }


}