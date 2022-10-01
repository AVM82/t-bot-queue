package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.Optional;


public class AddCityToProviderCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCityToProviderCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ProvideRepository provideRepository;
    private final BundleLanguage bundleLanguage;

    public AddCityToProviderCommand(SendBotMessageService sendBotMessageService, ProvideRepository provideRepository, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.provideRepository = provideRepository;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        LOGGER.info("Added city to provider");
        ProviderEntity providerEntity = null;
//        Optional<ProviderEntity> providerEntity1 = provideRepository.findById(update.getMessage().getChatId());
        Optional<ProviderEntity> providerEntity1 =  provideRepository
                .findPleaseProviderEntitiesByIdTelegramAAndName(update.getMessage().getChatId(), "lol");
        if (providerEntity1.isPresent()) {
         providerEntity = providerEntity1.get();
        }
        LOGGER.info("========= {}", providerEntity);
        providerEntity.setCity(update.getMessage().getText());
        provideRepository.save(providerEntity);
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Provider registered");

        if (ServiceCache.justRegistrated) {
            Long idTelegram = update.getMessage().getChatId();
            String message = bundleLanguage.getValue(idTelegram, "input_name_service");
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(message).build());
            LOGGER.info("Add new service.");
            ServiceCache.justRegistrated = false;
        }
        return true;
    }
}
