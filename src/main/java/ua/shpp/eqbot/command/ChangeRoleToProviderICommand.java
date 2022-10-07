package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;


public class ChangeRoleToProviderICommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeRoleToProviderICommand.class);

    private final SendBotMessageService sendBotMessageService;

    ProviderRepository providerRepository;
    private final BundleLanguage bundleLanguage;

    public ChangeRoleToProviderICommand(SendBotMessageService sendBotMessageService, ProviderRepository providerRepository, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.providerRepository = providerRepository;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        if (providerRepository.findByTelegramId(update.getMessage().getChatId()) != null)/*providerRepository.findById(update.getMessage().getChatId())*/ {
            LOGGER.info("Find provider with such id and enroll as a provider");
            sendBotMessageService.sendMessage(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(bundleLanguage.getValue(update.getMessage().getChatId(), "switch_to_provider"))
                    .build());
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> buttonCreate = new ArrayList<>();
            buttonCreate.add(InlineKeyboardButton.builder()
                    .text(bundleLanguage.getValue(update.getMessage().getChatId(), "create_new_provider"))
                    .callbackData("add_provider")
                    .build());
            keyboard.add(buttonCreate);
            inlineKeyboardMarkup.setKeyboard(keyboard);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(bundleLanguage.getValue(update.getMessage().getChatId(), "choose_menu_option"));
            sendMessage.setChatId(update.getMessage().getChatId());
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendBotMessageService.sendMessage(sendMessage);
        }
        return true;
    }
}
