package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.dto.ProviderDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.stage.PositionRegistrationProvider;

public class RegistrationNewProviderCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationNewProviderCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ProviderService providerService;
    private final BundleLanguage bundleLanguage;

    public RegistrationNewProviderCommand(SendBotMessageService sendBotMessageService, ProviderService providerService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.providerService = providerService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        Long id;
        if(update.hasCallbackQuery())
            id = update.getCallbackQuery().getFrom().getId();
        else if(update.hasMessage())
            id = update.getMessage().getChatId();
        else
            return false;
        ProviderDto providerDto = providerService.getProviderDto(id);
        LOGGER.info("i try register new provider");
        boolean isRegistration = false;
        if (providerDto == null) {
            providerService.saveProviderDto(generateProviderFromMessage(id));
            assert id != null;
            sendBotMessageService.sendMessage(SendMessage.builder()
                    .chatId(id)
                    .text(bundleLanguage.getValue(id, "company_name"))
                    .build());
        } else {
            switch (providerService.getProviderDto(id).getPositionRegistrationProvider()) {
                case INPUT_COMPANY_NAME:
                    LOGGER.info("new provider phase INPUT_USERNAME with message text {}", update.getMessage().getText());
                    if (update.getMessage() != null && !update.getMessage().isCommand()) {
                    providerDto.setName(update.getMessage().getText())
                            .setPositionRegistrationProvider(PositionRegistrationProvider.INPUT_CITY);
                    providerService.saveProviderDto(providerDto);
                    sendBotMessageService.sendMessage(SendMessage.builder()
                            .chatId(id)
                            .text(bundleLanguage.getValue(id, "input_city"))
                            .build());
                    }
                    break;
                case INPUT_CITY:
                    LOGGER.info("new provider phase INPUT_CITY with message text {}", update.getMessage().getText());
                    if (update.getMessage() != null && !update.getMessage().isCommand()) {
                    providerDto.setCity(update.getMessage().getText())
                            .setPositionRegistrationProvider(PositionRegistrationProvider.DONE);
                    providerService.saveDtoInDataBase(providerDto);
                    /*sendBotMessageService.sendMessage(SendMessage.builder()
                            .chatId(id)
                            .text(bundleLanguage.getValue(id, "provider_registered"))
                            .build());*/
                    isRegistration =true;
                    }
                    break;
                default:
                    //do nothing
                    break;
            }
        }
        return isRegistration;
    }

    private ProviderDto generateProviderFromMessage(Long id) {
        ProviderDto providerDto = new ProviderDto();
        providerDto.setTelegramId(id)
                .setPositionRegistrationProvider(PositionRegistrationProvider.INPUT_COMPANY_NAME);
        return providerDto;
    }
}
