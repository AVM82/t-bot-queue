package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.model.repository.ProviderRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

public class RegistrationNewProviderCommand implements Command{

    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationNewProviderCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ProviderRepository providerRepository;

    public RegistrationNewProviderCommand(SendBotMessageService sendBotMessageService, ProviderRepository providerRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.providerRepository= providerRepository;
    }

    @Override
    public boolean execute(Update update) {
        LOGGER.info("Registered new Provider");
        providerRepository.saveProvider(update.getMessage().getChatId());
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),"Provider registered");
        return true;
    }
}
