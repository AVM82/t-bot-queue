package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.BotCommandResultDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

@Component
public class SettingsBotCommand implements BotCommand {

    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public SettingsBotCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public BotCommandResultDto execute(Update update) {
        BotCommandResultDto resultDto = new BotCommandResultDto();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonCreate = new ArrayList<>();
        buttonCreate.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(update.getMessage().getChatId(), "change_role_to_provider"))
                .callbackData("change_role")
                .build());
        keyboard.add(buttonCreate);
        List<InlineKeyboardButton> buttonLang = new ArrayList<>();
        buttonCreate.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(update.getMessage().getChatId(), "change_language"))
                .callbackData("change_lang")
                .build());
        keyboard.add(buttonLang);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(bundleLanguage.getValue(update.getMessage().getChatId(), "choose_menu_option"));
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
        return resultDto.setDone(true);
    }
}
