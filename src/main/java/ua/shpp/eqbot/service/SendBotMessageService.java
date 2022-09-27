package ua.shpp.eqbot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Service for sending messages via telegram-bot.
 */
public interface SendBotMessageService {

    /**
     * Send message via telegram bot.
     *
     * @param chatId  provided chatId in which messages would be sent.
     * @param message provided message to be sent.
     */
    void sendMessage(String chatId, String message);

    void sendMessage(SendMessage message);

    void setReplyMarkup(String chatId, ReplyKeyboardMarkup replyKeyboardMarkup);
}