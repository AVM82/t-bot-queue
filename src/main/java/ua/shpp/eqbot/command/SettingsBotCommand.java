package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.ArrayList;
import java.util.List;

@Component
public class SettingsBotCommand implements BotCommand {

    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    @Value("${telegram.bot.name}")
    private String botUsername;

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsBotCommand.class);

    @Autowired
    public SettingsBotCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
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
        List<InlineKeyboardButton> buttonShare = new ArrayList<>();
        String sharingMessage = "\n%seSecretar, %s%s%s \n\n%s%s%s         https://t.me/%s         %s%s%s";
        sharingMessage = String.format(sharingMessage, Icon.WOMAN_OFFICE_WORKER.get(),
                                                        bundleLanguage.getValue(update.getMessage().getChatId(), "share_message_first_part"),
                                                        Icon.CLIPBOARD.get(), bundleLanguage.getValue(update.getMessage().getChatId(), "share_message_second_part"),
                                                        Icon.ARROW_RIGHT.get(), Icon.ARROW_RIGHT.get(),
                                                        Icon.ARROW_RIGHT.get(), botUsername,
                                                        Icon.ARROW_LEFT.get(), Icon.ARROW_LEFT.get(),
                                                        Icon.ARROW_LEFT.get());
        buttonShare.add(InlineKeyboardButton.builder()
                .text(Icon.MAN_RAISING_HAND.get() + "    " + bundleLanguage.getValue(update.getMessage().getChatId(), "share_bot")
                        + "    " + Icon.INCOMING_ENVELOPE.get())
                .switchInlineQuery(sharingMessage)
                .build());
        keyboard.add(buttonShare);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(bundleLanguage.getValue(update.getMessage().getChatId(), "choose_menu_option"));
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
        LOGGER.info("Send settings menu");

        return true;
    }
}
