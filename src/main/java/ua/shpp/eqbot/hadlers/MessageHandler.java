package ua.shpp.eqbot.hadlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.shpp.eqbot.cache.Cache;
import ua.shpp.eqbot.entity.Position;
import ua.shpp.eqbot.entity.UserEntity;
import ua.shpp.eqbot.messagesender.MessageSender;


@Component
public class MessageHandler implements Handler<Message> {
    public MessageHandler(Cache<UserEntity> cache) {
        this.cache = cache;
    }

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
            switch (user.getPosition()){
                case INPUT_USERNAME:
                    user.setName(message.getText());
                    user.setPosition(Position.INPUT_CITY);
                    messageSender.sendMessage(
                           SendMessage.builder()
                                   .text("Введіть назву вашого міста")
                                   .chatId(String.valueOf(message.getChatId()))
                                   .build()
                    );
                    break;
                case INPUT_CITY:
                    user.setCity(message.getText());
                    user.setPosition(Position.NONE);
                    messageSender.sendMessage(
                            SendMessage.builder()
                                    .text("id "+user.getId()+
                                            "\nім'я "+ user.getName()+
                                            "\nмісто "+ user.getCity())
                                    .chatId(String.valueOf(message.getChatId()))
                                    .build()
                    );
                    break;
            }
        }else if (message.hasText()) {
            if (message.getText().equals("/start")) {
                cache.add(generateUserFromMessage(message));
                messageSender.sendMessage(SendMessage.builder()
                        .text("Потрібна реестрація\nвведіть ім'я")
                        .chatId(String.valueOf(message.getChatId()))
                                .build());
            }else {
                messageSender.sendMessage(SendMessage.builder()
                        .text("Ви зареестровані")
                        .chatId(String.valueOf(message.getChatId()))
                        .build());
            }
        }
    }
}
