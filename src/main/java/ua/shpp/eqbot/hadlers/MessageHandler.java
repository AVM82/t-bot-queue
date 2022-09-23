package ua.shpp.eqbot.hadlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.shpp.eqbot.cache.Cache;
import ua.shpp.eqbot.entity.Position;
import ua.shpp.eqbot.entity.UserEntity;
import ua.shpp.eqbot.messagesender.MessageSender;
import ua.shpp.eqbot.service.RegistrationUser;

@Component
public class MessageHandler implements Handler<Message> {
    public MessageHandler(Cache<UserEntity> cache) {
        this.cache = cache;
    }

    @Autowired
    public void setRegistrationUser(RegistrationUser registrationUser) {
        this.registrationUser = registrationUser;
    }

    private RegistrationUser registrationUser;

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    private MessageSender messageSender;
    private final Cache<UserEntity> cache;

    private UserEntity generateUserFromMessage(Message message){
        UserEntity user = new UserEntity();
        user.setName(message.getFrom().getUserName())
                .setId(message.getChatId())
                .setPosition(Position.INPUT_USERNAME);
        return user;
    }
    @Override
    public void choose(Message message) {
        UserEntity user = cache.findBy(message.getChatId());
        if(user != null){

        }else if (message.hasText()) {
            if (message.getText().equals("/start")) {
                cache.add(generateUserFromMessage(message));

                /*if(!registrationUser.changeregistrationUser(message.getFrom().getId())) {*/
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(String.valueOf(message.getChatId()));
                    sendMessage.setText("Потрібна реестрація\n" +
                            "введіть місто");

                    //registrationUser.registration();
                    messageSender.sendMessage(sendMessage);
                //}
            }
        }
    }
}
