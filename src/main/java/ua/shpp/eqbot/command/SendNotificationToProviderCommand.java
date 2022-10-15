package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

public class SendNotificationToProviderCommand implements ICommand {
    private final ServiceRepository serviceRepository;
    private final SendBotMessageService sendBotMessageService;

    public SendNotificationToProviderCommand(ServiceRepository serviceRepository, SendBotMessageService sendBotMessageService) {
        this.serviceRepository = serviceRepository;
        this.sendBotMessageService = sendBotMessageService;
    }

    public void sendNotification(Long serviceId, String timeOfRegistration, String userName, String language) {
        ServiceEntity serviceEntity = serviceRepository.findFirstById(serviceId);
        Long providerChatId = serviceEntity.getTelegramId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(providerChatId);
        if (language.equals("en")) {
            sendMessage.setText("New customer " + userName + " registered to your service: " + serviceEntity.getName() + " at time: " + timeOfRegistration);
        } else {
            sendMessage.setText("Новий клієнт " + userName + " заруєструвався до вашого сервісу: " + serviceEntity.getName() + " на дату: " + timeOfRegistration);
        }
        sendBotMessageService.sendMessage(sendMessage);
    }

    @Override
    public boolean execute(Update update) {
        return true;
    }
}