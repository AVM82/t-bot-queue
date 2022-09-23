package ua.shpp.eqbot.messagesender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

@Component
public class MessageSenderImpl implements MessageSender{
    private EqTelegramBot bot;

    @Override
    public void sendMessage(SendMessage message) {
        try {
            bot.execute(message);
        }catch (TelegramApiException e) {
            //TODO logger
        }
    }

    @Autowired
    public void setBot(EqTelegramBot bot) {
        this.bot = bot;
    }

}
