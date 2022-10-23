package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.dto.ProviderDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.List;

@Component("deleteBotCommand")
public class DeleteUserBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserBotCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;
    private final ProviderService providerService;
    private final ServiceRepository serviceRepository;

    @Autowired
    public DeleteUserBotCommand(SendBotMessageService sendBotMessageService, UserService userService,
                                BundleLanguage bundleLanguage, ProviderService providerService,
                                ServiceRepository serviceRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
        this.providerService = providerService;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public boolean execute(Update update) {
        List<ServiceEntity> serviceEntityList = serviceRepository.findAllByTelegramId(update.getMessage().getChatId());
        if (!serviceEntityList.isEmpty()) {
            LOGGER.info("deleted service in database");
            serviceRepository.deleteAllInBatch(serviceEntityList);
        }
        //delete service in cache
        ServiceCache.remove(update.getMessage().getChatId());

        ProviderEntity providerEntity = providerService.getByTelegramIdEntity(update.getMessage().getChatId());
        if (providerEntity != null) {
            LOGGER.info("deleted provider in database");
            providerService.removeInDataBase(providerEntity);
        }

        ProviderDto providerDto = providerService.getProviderDto(update.getMessage().getChatId());
        if (providerDto != null) {
            LOGGER.info("deleted provider in cache");
            providerService.remove(update.getMessage().getChatId());
        }

        userService.remove(update.getMessage().getChatId());

        sendBotMessageService.sendMessage(SendMessage.builder()
                .text(Icon.WHITE_CHECK_MARK.get() + bundleLanguage.getValue(update.getMessage().getChatId(), "user_deleted"))
                .chatId(update.getMessage().getChatId())
                .build());

        return true;
    }
}
