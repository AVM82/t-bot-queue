package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

import java.util.List;


public class DeleteUserCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;
    private final ProviderRepository providerRepository;
    private final ServiceRepository serviceRepository;

    public DeleteUserCommand(SendBotMessageService sendBotMessageService, UserRepository userRepository,
                             UserService userService, BundleLanguage bundleLanguage, ProviderRepository providerRepository, ServiceRepository serviceRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
        this.providerRepository = providerRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public boolean execute(Update update) {
        List<ServiceEntity> serviceEntityList = serviceRepository.findAllByIdTelegram(update.getMessage().getChatId());
        if (!serviceEntityList.isEmpty()) {
            LOGGER.info("deleted service");
            serviceRepository.deleteAllInBatch(serviceEntityList);
        }

        List<ProviderEntity> providerEntityList = providerRepository.findAllByIdTelegram(update.getMessage().getChatId());
        if (!providerEntityList.isEmpty()) {
            LOGGER.info("deleted provider");
            providerRepository.deleteAllInBatch(providerEntityList);
        }

        userService.remove(update.getMessage().getChatId());

        sendBotMessageService.sendMessage(SendMessage.builder()
                .text(bundleLanguage.getValue(update.getMessage().getChatId(), "user_deleted"))
                .chatId(update.getMessage().getChatId())
                .build());

        return true;
    }
}
