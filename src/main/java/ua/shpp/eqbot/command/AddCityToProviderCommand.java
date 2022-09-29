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



public class AddCityToProviderCommand implements Command{
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCityToProviderCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ProvideRepository provideRepository;


    public AddCityToProviderCommand(SendBotMessageService sendBotMessageService, ProvideRepository provideRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.provideRepository = provideRepository;
    }

    @Override
    public boolean execute(Update update) {
        LOGGER.info("Added city to provider");
        ProviderEntity providerEntity = provideRepository.findFirstById_telegram(update.getMessage().getChatId());
        providerEntity.setCity(update.getMessage().getText());
        provideRepository.save(providerEntity);
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),"Provider registered");

        if(ServiceCache.justRegistrated){
            Long idTelegram = update.getMessage().getChatId();
            String message = BundleLanguage.getValue(idTelegram, "input_name_service");
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(message).build());
            LOGGER.info("Add new service.");
            ServiceCache.justRegistrated=false;
        }
        return true;
    }
}
