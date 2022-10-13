package ua.shpp.eqbot.command.registrationfortheservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

public class ShowDaysRegistrationForTheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowDaysRegistrationForTheService.class);
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    public ShowDaysRegistrationForTheService(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    /*public void showDays(){

    }*/


    public void addRequest(Long id) {
        LOGGER.info("add buttons to the service search");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(createButton(id, "change_provider_details", "change_provider_details"));
        keyboard.add(createButton(id, "newServiceFromAnExistingProvider", "newServiceFromAnExistingProvider"));
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
        button.add(InlineKeyboardButton.builder().text(bundleLanguage.getValue(id, nameButton)).callbackData(dataButton).build());
        return button;
    }
}
