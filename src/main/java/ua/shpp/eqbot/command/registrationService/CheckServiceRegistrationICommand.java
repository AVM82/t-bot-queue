package ua.shpp.eqbot.command.registrationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.command.ICommand;
import ua.shpp.eqbot.dto.ServiceDTO;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.List;

public class CheckServiceRegistrationICommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckServiceRegistrationICommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;
    private final ServiceRepository serviceRepository;
    private final ImageService imageService;

    public CheckServiceRegistrationICommand(
            SendBotMessageService sendBotMessageService,
            BundleLanguage bundleLanguage,
            ServiceRepository serviceRepository,
            ImageService imageService) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
        this.serviceRepository = serviceRepository;
        this.imageService = imageService;
    }

    @Override
    public boolean execute(Update update) {
        Long id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else if (update.hasMessage()) {
            id = update.getMessage().getChatId();
        } else {
            return false;
        }
        ServiceDTO serviceDTO = ServiceCache.findBy(id);
        if (serviceDTO == null) {
            LOGGER.info("the provider is not in the cache");
            List<ServiceEntity> serviceEntityList = serviceRepository.findAllByTelegramId(id);
            if (!serviceEntityList.isEmpty()) {
                LOGGER.info("there is provider in the database");
                return true;
            }
            return new RegistrationServiceICommand(sendBotMessageService, serviceRepository, imageService, bundleLanguage).execute(update);
        }

        return false;
    }
}
