package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

public class RegistrationNewProviderCommand implements Command{

    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationNewProviderCommand.class);
    private final SendBotMessageService sendBotMessageService;

    public RegistrationNewProviderCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {
        LOGGER.info("Registered new Provider");
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),"Type Companion's Name");
        return true;
    }
}
