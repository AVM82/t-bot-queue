package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

@Component("changeRoleBotCommand")
public class ChangeRoleToProviderBotCommand implements BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeRoleToProviderBotCommand.class);

    private final SendBotMessageService sendBotMessageService;

    private final ProviderRepository providerRepository;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public ChangeRoleToProviderBotCommand(
            SendBotMessageService sendBotMessageService,
            ProviderRepository providerRepository,
            BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.providerRepository = providerRepository;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {

        Long id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else if (update.hasMessage()) {
            id = update.getMessage().getChatId();
        } else {
            return false;
        }

        if (providerRepository.findByTelegramId(id) != null) {
            LOGGER.info("Find provider with such id and enroll as a provider");
            sendBotMessageService.sendMessage(
                    SendMessage.builder().chatId(id).text(bundleLanguage.getValue(id, "switch_to_provider")).build());
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> buttonCreate = new ArrayList<>();
            buttonCreate.add(InlineKeyboardButton.builder()
                    .text(bundleLanguage.getValue(id, "create_new_provider"))
                    .callbackData("add_provider").build());
            keyboard.add(buttonCreate);
            inlineKeyboardMarkup.setKeyboard(keyboard);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(bundleLanguage.getValue(id, "choose_menu_option"));
            sendMessage.setChatId(id);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendBotMessageService.sendMessage(sendMessage);
        }
        return true;
    }
}
