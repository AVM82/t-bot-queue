package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

public class ServiceInfoCommand implements ICommand {
    private final ServiceRepository serviceRepository;
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    public ServiceInfoCommand(ServiceRepository serviceRepository, SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.serviceRepository = serviceRepository;
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    private void sendServiceInfoToCreator(ServiceEntity service) {
        String message = messageInfo(service, service.getTelegramId());
    }

    private void sendServiceInfo(ServiceEntity service, Long chatId) {
        String message = messageInfo(service, chatId);
    }

    private String messageInfo(ServiceEntity service, Long chatId) {
        StringBuilder sb = new StringBuilder("**").append(service.getName()).append("**");
        sb.append("\n");
        sb.append(service.getDescription());
        sb.append("\n");
        String providerMessage = bundleLanguage.getValue(chatId, "service_provider");
        sb.append("[").append(providerMessage).append("]");
        sb.append("(tg://user?id=").append(service.getTelegramId()).append(")");
        return sb.toString();
    }

    @Override
    public boolean execute(Update update) {
        String serviceId = update.getMessage().getText().split("/")[1];
        ServiceEntity service = serviceRepository.findFirstById(Long.parseLong(serviceId));
        Long chatId = update.getMessage().getChatId();
        if (service.getTelegramId().equals(chatId)) {
            sendServiceInfoToCreator(service);
        } else {
            sendServiceInfo(service, chatId);
        }
        return true;
    }
}