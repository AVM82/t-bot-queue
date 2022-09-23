package ua.shpp.eqbot.hadlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.shpp.eqbot.messagesender.MessageSender;

@Component
public class MessageHandler implements Handler<Message>{

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    private MessageSender messageSender;
    @Override
    public void choose(Message message) {
        if(message.hasText()){
            if(message.getText().equals("/start")){

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText("Потрібна реестрація\n" +
                        "введіть свое ім'я");
                messageSender.sendMessage(sendMessage);

            }
        }
    }
}
