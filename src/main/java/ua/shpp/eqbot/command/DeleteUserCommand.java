package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.BotUserCache;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.List;


public class DeleteUserCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final UserRepository userRepository;
    private final ProvideRepository provideRepository;
    private final ServiceRepository serviceRepository;

    public DeleteUserCommand(SendBotMessageService sendBotMessageService, UserRepository userRepository,
                             ProvideRepository provideRepository, ServiceRepository serviceRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.userRepository = userRepository;
        this.provideRepository = provideRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public boolean execute(Update update) {
        List<ServiceEntity> serviceEntityList = serviceRepository.findAllByIdTelegram(update.getMessage().getChatId());
        if (!serviceEntityList.isEmpty()) {
            LOGGER.info("deleted service");
            serviceRepository.deleteAllInBatch(serviceEntityList);
        }

        List<ProviderEntity> providerEntityList = provideRepository.findAllByIdTelegram(update.getMessage().getChatId());
        if (!providerEntityList.isEmpty()) {
            LOGGER.info("deleted provider");
            provideRepository.deleteAllInBatch(providerEntityList);
        }

        UserEntity userEntity = userRepository.findFirstByIdTelegram(update.getMessage().getChatId());
        if (userEntity != null) {
            LOGGER.info("deleted user");
            userRepository.delete(userEntity);
        }

        UserDto userDto = BotUserCache.findBy(update.getMessage().getChatId());
        if (userDto != null) {
            LOGGER.info("deleted user in cache");
            sendBotMessageService.sendMessage(SendMessage.builder()
                    .text(BundleLanguage.getValue(update.getMessage().getChatId(), "user_deleted"))
                    .chatId(update.getMessage().getChatId())
                    .build());
            BotUserCache.remove(userDto);
        }
        return true;
    }
}
