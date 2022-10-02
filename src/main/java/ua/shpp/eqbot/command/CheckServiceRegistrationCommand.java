package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderDto;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.List;

public class CheckServiceRegistrationCommand implements Command{
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckServiceRegistrationCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;
    private final ServiceRepository serviceRepository;

    public CheckServiceRegistrationCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage, ServiceRepository serviceRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public boolean execute(Update update) {
        Long id;
        if (update.hasCallbackQuery())
            id = update.getCallbackQuery().getFrom().getId();
        else if (update.hasMessage())
            id = update.getMessage().getChatId();
        else
            return false;
        ServiceDTO serviceDTO = ServiceCache.findBy(id);
        if (serviceDTO == null) {
            LOGGER.info("the provider is not in the cache");
            List<ServiceEntity> serviceEntityList = serviceRepository.findAllByIdTelegram(id);
            if (!serviceEntityList.isEmpty()) {
                LOGGER.info("there is provider in the database");
                sendBotMessageService.sendMessage(SendMessage.builder()
                        .chatId(id)
                        .text(bundleLanguage.getValue(id, "registered_with_a_provider"))
                        .build());
                printListService(id);
                return true;
            }
            sendBotMessageService.sendMessage(SendMessage.builder()
                    .chatId(id)
                    .text(bundleLanguage.getValue(id, "no_registration_provider"))
                    .build());
            //return new AddService(sendBotMessageService, providerService, bundleLanguage).execute(update);
        }
        //return new AddNewProviderCommand(sendBotMessageService, providerService, bundleLanguage).execute(update);
        return false;
    }

    private void printListService(Long id) {
        List<ServiceEntity> serviceEntityList = serviceRepository.findAllByIdTelegram(id);
        for (ServiceEntity entity : serviceEntityList) {
            sendBotMessageService.sendMessage(SendMessage.builder()
                    .chatId(id)
                    .text(bundleLanguage.getValue(id, "name_service") + ": " + entity.getName())
                    .build());
        }
    }
}
