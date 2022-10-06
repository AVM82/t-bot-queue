package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.ProviderDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.stage.PositionRegistrationProvider;

import java.util.ArrayList;
import java.util.List;

public class CheckProviderRegistrationICommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckProviderRegistrationICommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ProviderService providerService;
    private final BundleLanguage bundleLanguage;

    public CheckProviderRegistrationICommand(SendBotMessageService sendBotMessageService,
                                             ProviderService providerService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.providerService = providerService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        Long id;
        if (update.hasCallbackQuery())
            id = update.getCallbackQuery().getFrom().getId();
        else if (update.hasMessage())
            id = update.getMessage().getChatId();
        else
            return false;
        ProviderDto providerDto = providerService.getProviderDto(id);
        if (providerDto == null) {
            LOGGER.info("the provider is not in the cache");
            ProviderEntity providerEntity = providerService.getByTelegramIdEntity(id);
            if (providerEntity != null) {
                LOGGER.info("there is provider in the database");
                providerService.saveEntityInCache(providerEntity);
                printListProvider(id);
                addRequest(id);
                return true;
            }
            sendBotMessageService.sendMessage(SendMessage.builder()
                    .chatId(id)
                    .text(bundleLanguage.getValue(id, "no_registration_provider"))
                    .build());
            return new RegistrationNewProviderICommand(sendBotMessageService, providerService, bundleLanguage).execute(update);
        }
        if (providerDto.getPositionRegistrationProvider() == PositionRegistrationProvider.DONE) {
            printListProvider(id);
            addRequest(id);
            return true;
        } else
            return new RegistrationNewProviderICommand(sendBotMessageService, providerService, bundleLanguage).execute(update);
    }

    private void printListProvider(Long id) {
        ProviderDto dto = providerService.getProviderDto(id);
        sendBotMessageService.sendMessage(SendMessage.builder()
                .chatId(id)
                .text(bundleLanguage.getValue(id, "registered_to_you") +
                        "\n" + bundleLanguage.getValue(id, "company_name") + ": " + dto.getName() +
                        "\n" + bundleLanguage.getValue(id, "city") + ": " + dto.getCity())
                .build());
    }

    private void addRequest(Long id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(createButton(id, "change_provider_details", "change_provider_details"));
        keyboard.add(createButton(id, "newServiceFromAnExistingProvider",
                "newServiceFromAnExistingProvider"));
        keyboard.add(createButton(id, "return_in_menu", "return_in_menu"));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(bundleLanguage.getValue(id, "choose_menu_option"));
        sendMessage.setChatId(id);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
    }

    private List<InlineKeyboardButton> createButton(Long id, String nameButton, String dataButton) {
        List<InlineKeyboardButton> button = new ArrayList<>();
        button.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(id, nameButton))
                .callbackData(dataButton)
                .build());
        return button;
    }
}
