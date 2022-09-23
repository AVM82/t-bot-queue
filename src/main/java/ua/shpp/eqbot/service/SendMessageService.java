package ua.shpp.eqbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.shpp.eqbot.messagesender.MessageSender;

@Service
public class SendMessageService {
    @Autowired
    public void setSender(MessageSender sender) {
        this.sender = sender;
    }

    private MessageSender sender;

    public void test1(Message message) {

        sender.sendMessage(SendMessage.builder()
                .text("бла бла")
                .chatId(message.getChatId())
                .build());
    }
}
