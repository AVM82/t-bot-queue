package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;


public class ChangeRoleToProviderCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeRoleToProviderCommand.class);

    private final SendBotMessageService sendBotMessageService;

    ProvideRepository provideRepository;

    public ChangeRoleToProviderCommand(SendBotMessageService sendBotMessageService, ProvideRepository provideRepository) {
        this.sendBotMessageService = sendBotMessageService;

        this.provideRepository = provideRepository;
    }

    @Override
    public boolean execute(Update update) {
        if (provideRepository.findById(update.getMessage().getChatId()).isPresent())/*providerRepository.findById(update.getMessage().getChatId())*/ {
            LOGGER.info("Find provider with such id and enroll as a provider");
            sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "U switched to provider");
        } else {
            var markup = new ReplyKeyboardMarkup();
            var keyboardRows = new ArrayList<KeyboardRow>();
            KeyboardRow registrationNewProvider = new KeyboardRow();
            registrationNewProvider.add("Реєстрація нового провайдера");
            keyboardRows.add(registrationNewProvider);
            markup.setKeyboard(keyboardRows);
            markup.setResizeKeyboard(true);
            LOGGER.info("Didn't find provider with such id");
            sendBotMessageService.setReplyMarkup(update.getMessage().getChatId().toString(), markup);
        }
        return true;
    }
}
