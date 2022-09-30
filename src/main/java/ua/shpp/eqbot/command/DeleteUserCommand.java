package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;


public class DeleteUserCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;
    private final ProvideRepository provideRepository;
    private final ServiceRepository serviceRepository;

    public DeleteUserCommand(SendBotMessageService sendBotMessageService, UserRepository userRepository,
                             UserService userService, BundleLanguage bundleLanguage, ProvideRepository provideRepository, ServiceRepository serviceRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
        this.provideRepository = provideRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public boolean execute(Update update) {
        ServiceEntity serviceEntity = serviceRepository.findByIdTelegram(update.getMessage().getChatId());
        if (serviceEntity != null) {
            LOGGER.info("deleted service");
            serviceRepository.delete(serviceEntity);
        }

        ProviderEntity providerEntity = provideRepository.findFirstByIdTelegram(update.getMessage().getChatId());
        if (providerEntity != null) {
            LOGGER.info("deleted provider");
            provideRepository.delete(providerEntity);
        }

        userService.remove(update.getMessage().getChatId());

        sendBotMessageService.sendMessage(SendMessage.builder()
                .text(bundleLanguage.getValue(update.getMessage().getChatId(), "user_deleted"))
                .chatId(update.getMessage().getChatId())
                .build());

        return true;
    }
}
