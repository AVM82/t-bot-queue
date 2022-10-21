package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.dto.BotCommandResultDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

public class SendNotificationToProviderCommand implements BotCommand {
    private final ServiceRepository serviceRepository;
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    public SendNotificationToProviderCommand(ServiceRepository serviceRepository, SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.serviceRepository = serviceRepository;
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    public void sendNotification(Long serviceId, String timeOfRegistration, String userName, String customerTelegramId) {
        ServiceEntity serviceEntity = serviceRepository.findFirstById(serviceId);
        Long providerChatId = serviceEntity.getTelegramId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(providerChatId);
        String internationalizationAnswer = bundleLanguage.getValue(providerChatId, "send_notification_to_provider");
        String[] answer = internationalizationAnswer.split("\\.");
        sendMessage.setText(answer[0] + userName + answer[1] + serviceEntity.getName() + answer[2] + timeOfRegistration);
        String connectMessage = bundleLanguage.getValue(providerChatId, "connect_with_customer");
        sendMessage = sendBotMessageService.sendButtonToUser(sendMessage, customerTelegramId, connectMessage);
        sendBotMessageService.sendMessage(sendMessage);
    }

    @Override
    public BotCommandResultDto execute(Update update) {
        return new BotCommandResultDto().setDone(true);
    }
}