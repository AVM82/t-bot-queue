package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.PrevPositionDTO;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Start {@link BotCommand}.
 */
@Component
public class StartBotCommand implements BotCommand {

    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;
    private final UserService userService;

    @Autowired
    public StartBotCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
        this.userService = userService;
    }

    @Override
    public boolean execute(Update update) {
        long telegramId;
        if (update.hasCallbackQuery()) {
            telegramId = update.getCallbackQuery().getFrom().getId();
        } else {
            telegramId = update.getMessage().getChatId();
        }
        createStartMenu(telegramId);
        userService.putPrevPosition(new PrevPositionDTO(telegramId, PositionMenu.MENU_START, ""));
        UserDto userDto = userService.getDto(telegramId);
        userDto.setPositionMenu(PositionMenu.MENU_START);
        return true;
    }

    private void createStartMenu(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonCreate = new ArrayList<>();
        buttonCreate.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(chatId, "create_service"))
                .callbackData("create_service")
                .build());
        List<InlineKeyboardButton> buttonSearch = new ArrayList<>();
        buttonCreate.add(InlineKeyboardButton.builder()
                .text(bundleLanguage.getValue(chatId, "search_service"))
                .callbackData("search_service")
                .build());
        keyboard.add(buttonCreate);
        keyboard.add(buttonSearch);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(bundleLanguage.getValue(chatId, "choose_menu_option"));
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
    }
}
