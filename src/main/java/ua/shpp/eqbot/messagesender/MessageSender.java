package ua.shpp.eqbot.messagesender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {
    void sendMessage(SendMessage message);
}
