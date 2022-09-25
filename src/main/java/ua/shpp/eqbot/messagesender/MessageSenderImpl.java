package ua.shpp.eqbot.messagesender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

@Component
public class MessageSenderImpl implements MessageSender {
    Logger log = LoggerFactory.getLogger(MessageSenderImpl.class);
    private EqTelegramBot bot;

    @Override
    public void sendMessage(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.warn(e.getMessage());
        }
    }

    @Autowired
    public void setBot(EqTelegramBot bot) {
        this.bot = bot;
    }

}
