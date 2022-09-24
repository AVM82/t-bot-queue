package ua.shpp.eqbot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.shpp.eqbot.cache.Cache;
import ua.shpp.eqbot.entity.PositionMenu;
import ua.shpp.eqbot.entity.PositionRegistration;
import ua.shpp.eqbot.entity.UserDto;
import ua.shpp.eqbot.messagesender.MessageSender;

@Component
public class RegistrationUser {
    private final MessageSender messageSender;

    public RegistrationUser(MessageSender messageSender, Cache<UserDto> cache) {
        this.messageSender = messageSender;
        this.cache = cache;
    }

    private final Cache<UserDto> cache;

    private UserDto generateUserFromMessage(Message message) {
        UserDto user = new UserDto();
        user.setName(message.getFrom().getUserName())
                .setId(message.getChatId())
                .setPositionRegistration(PositionRegistration.INPUT_USERNAME)
                .setPositionMenu(PositionMenu.MENU_START);
        return user;
    }

    public void registration(Message message) {
        UserDto user = cache.findBy(message.getChatId());
        if (user == null) {
            cache.add(generateUserFromMessage(message));
            messageSender.sendMessage(createQuery(message.getChatId(),
                    "Потрібна реестрація\nвведіть ім'я"));
        } else {
            switch (user.getPositionRegistration()) {
                case INPUT_USERNAME:
                    user.setName(message.getText());
                    user.setPositionRegistration(PositionRegistration.INPUT_CITY);
                    messageSender.sendMessage(createQuery(message.getChatId(),
                            "Введіть назву вашого міста"));
                    break;
                case INPUT_CITY:
                    user.setCity(message.getText());
                    user.setPositionRegistration(PositionRegistration.NONE);
                    messageSender.sendMessage(createQuery(message.getChatId(),
                            "Дякуємо! Ви зареестровані" +
                                    "\nid " + user.getId() +
                                    "\nім'я " + user.getName() +
                                    "\nмісто " + user.getCity()));
                    break;
            }
        }
    }

    private SendMessage createQuery(Long chatId, String text) {
        return SendMessage.builder()
                .text(text)
                .chatId(String.valueOf(chatId))
                .build();
    }
}
