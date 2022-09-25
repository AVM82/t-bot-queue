package ua.shpp.eqbot.telegrambot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;
import ua.shpp.eqbot.telegrambot.repository.ProviderRepository;
import ua.shpp.eqbot.telegrambot.service.SendBotMessageService;

import java.util.ArrayList;


public class ChangeRoleToProviderCommand implements Command {

    private final static Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);

    private final SendBotMessageService sendBotMessageService;
    private final ProviderRepository providerRepository;

    public ChangeRoleToProviderCommand(SendBotMessageService sendBotMessageService, ProviderRepository providerRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.providerRepository = providerRepository;
    }

    @Override
    public void execute(Update update) {
        if(providerRepository.findById(update.getMessage().getChatId())){
            LOGGER.info("Find provider with such id and enroll as a provider");
            sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),"U switched to provider");
        }else {
            var markup = new ReplyKeyboardMarkup();
            var keyboardRows = new ArrayList<KeyboardRow>();
            KeyboardRow registrationNewProvider = new KeyboardRow();
            registrationNewProvider.add("Реестрація нового провайдера");
            keyboardRows.add(registrationNewProvider);
            markup.setKeyboard(keyboardRows);
            markup.setResizeKeyboard(true);
            LOGGER.info("Didn`t find provider with such id");
            sendBotMessageService.setReplyMarkup(update.getMessage().getChatId().toString(), markup);
        }
    }
}
