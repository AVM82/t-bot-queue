package ua.shpp.eqbot.command.registeringyouruser;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.command.BotCommand;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

@Component("recordyouruserBotCommand")
public class RecordYourUserBotCommand implements BotCommand {

    private final BundleLanguage bundleLanguage;
    private final UserService userService;
    private final ServiceRepository serviceRepository;
    private final SendBotMessageService sendBotMessageService;

    public RecordYourUserBotCommand(BundleLanguage bundleLanguage,
                                    UserService userService,
                                    ServiceRepository serviceRepository,
                                    SendBotMessageService sendBotMessageService) {
        this.bundleLanguage = bundleLanguage;
        this.userService = userService;
        this.serviceRepository = serviceRepository;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {

        return false;
    }
}
