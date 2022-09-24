package ua.shpp.eqbot.hadlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.shpp.eqbot.messagesender.MessageSender;

@Component
public class CommandHandler {

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    private MessageSender messageSender;

    public void checkCommand(Message message){
        //TODO
    }
}
