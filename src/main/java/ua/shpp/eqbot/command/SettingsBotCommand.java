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
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.ArrayList;
import java.util.List;

@Component
public class SettingsBotCommand implements BotCommand {

    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;
    private final ProviderService providerService;

    @Value("${telegram.bot.name}")
    private String botUsername;

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsBotCommand.class);

    @Autowired
    public SettingsBotCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage, ProviderService providerService) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
        this.providerService = providerService;
    }

    @Override
    public boolean execute(Update update) {
        long telegramId = update.getMessage().getChatId();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonsCreateAndLang = new ArrayList<>();
        buttonsCreateAndLang.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(telegramId, "change_role_to_provider"))
                .callbackData("change_role")
                .build());
        buttonsCreateAndLang.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(telegramId, "change_language") + " "
                + Icon.ARROWS_COUNTERCLOCKWISE.get())
                .callbackData("change_lang")
                .build());
        keyboard.add(buttonsCreateAndLang);
        if (providerService.getProviderDto(telegramId) != null) {
            List<InlineKeyboardButton> blackListButtons = new ArrayList<>();
            blackListButtons.add(bundleLanguage.createButton(telegramId, "my_blacklist", "blacklist/main"));
            keyboard.add(blackListButtons);
        }
        List<InlineKeyboardButton> buttonShare = new ArrayList<>();
        String sharingMessage = "\n%s%s, %s  %s \n\n         https://t.me/%s         ";
        sharingMessage = String.format(sharingMessage, Icon.PENCIL.get(), botUsername,
                bundleLanguage.getValue(telegramId, "share_message_first_part"),
                bundleLanguage.getValue(telegramId, "share_message_second_part"),
                botUsername);
        buttonShare.add(InlineKeyboardButton.builder()
                .text(Icon.MAN_RAISING_HAND.get() + "    " + bundleLanguage.getValue(telegramId, "share_bot")
                        + "    " + Icon.INCOMING_ENVELOPE.get())
                .switchInlineQuery(sharingMessage)
                .build());
        keyboard.add(buttonShare);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(bundleLanguage.getValue(telegramId, "choose_menu_option"));
        sendMessage.setChatId(telegramId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
        LOGGER.info("Send settings menu");

        return true;
    }
}
