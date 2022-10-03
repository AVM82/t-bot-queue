package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

public class AddProviderNameCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddProviderNameCommand.class);
    private final SendBotMessageService sendBotMessageService;

    private final ProviderRepository providerRepository;

    public AddProviderNameCommand(SendBotMessageService sendBotMessageService, ProviderRepository providerRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.providerRepository = providerRepository;
    }

    @Override
    public boolean execute(Update update) {
        LOGGER.info("Call addNameProvider");
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setName(update.getMessage().getText());
        providerEntity.setTelegramId(update.getMessage().getChatId());
        providerRepository.save(providerEntity);
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Type city");
        return true;
    }
}
