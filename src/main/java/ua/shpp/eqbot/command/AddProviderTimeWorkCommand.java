package ua.shpp.eqbot.command;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.service.SendBotMessageService;

public class AddProviderTimeWorkCommand implements Command{
    private static final Logger LOGGER = LoggerFactory.getLogger(AddProviderTimeWorkCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    private final ModelMapper modelMapper = new ModelMapper();

    public AddProviderTimeWorkCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage, ServiceCache serviceCache) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }


    @Override
    public boolean execute(Update update) {
        ServiceDTO serviceDTO = ServiceCache.findBy(update.getMessage().getChatId());

        return false;
    }

}
