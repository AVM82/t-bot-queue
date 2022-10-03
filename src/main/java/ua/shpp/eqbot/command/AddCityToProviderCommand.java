package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.service.SendBotMessageService;


public class AddCityToProviderCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCityToProviderCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ProviderRepository providerRepository;
    private final BundleLanguage bundleLanguage;

    public AddCityToProviderCommand(SendBotMessageService sendBotMessageService, ProviderRepository providerRepository, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.providerRepository = providerRepository;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        LOGGER.info("Added city to provider");
        ProviderEntity providerEntity = providerRepository.findFirstByTelegramId(update.getMessage().getChatId());
        providerEntity.setProviderCity(update.getMessage().getText());
        providerRepository.save(providerEntity);
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Provider registered");

        if (ServiceCache.justRegistrated) {
            Long telegramId = update.getMessage().getChatId();
            String message = bundleLanguage.getValue(telegramId, "input_name_service");
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(telegramId).text(message).build());
            LOGGER.info("Add new service.");
            ServiceCache.justRegistrated = false;
        }
        return true;
    }
}
