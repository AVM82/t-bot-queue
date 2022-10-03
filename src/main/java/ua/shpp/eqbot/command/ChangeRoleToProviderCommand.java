package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;


public class ChangeRoleToProviderCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeRoleToProviderCommand.class);

    private final SendBotMessageService sendBotMessageService;

    ProviderRepository providerRepository;

    public ChangeRoleToProviderCommand(SendBotMessageService sendBotMessageService, ProviderRepository providerRepository) {
        this.sendBotMessageService = sendBotMessageService;

        this.providerRepository = providerRepository;
    }

    @Override
    public boolean execute(Update update) {
        if (providerRepository.findByIdTelegram(update.getMessage().getChatId()) != null)/*providerRepository.findById(update.getMessage().getChatId())*/ {
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
