package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchById implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchById.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public SearchById(SendBotMessageService sendBotMessageService,
                      ServiceRepository serviceRepository,
                      UserService userService,
                      BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        long id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else {
            id = update.getMessage().getChatId();
        }

        UserDto user = userService.getDto(id);
        if (user.getPositionMenu() != PositionMenu.SEARCH_BY_ID) {
            user.setPositionMenu(PositionMenu.SEARCH_BY_ID);
            sendBotMessageService.sendMessage(String.valueOf(id), bundleLanguage.getValue(id, "search.searchId.text"));
            return true;
        } else {
            String idService = update.getMessage().getText();
            ServiceEntity result = idService.matches("\\d+") ? serviceRepository.findFirstById(Long.valueOf(idService)) : null;
            if (result != null) {
                LOGGER.info("Found service by id");
                user.setPositionMenu(PositionMenu.SEARCH_BY_NAME);
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> availableServiceButtons = new ArrayList<>();
                List<InlineKeyboardButton> button = new ArrayList<>();
                button.add(InlineKeyboardButton.builder()
                        .text(result.getName())
                        .callbackData("service_info" + result.getId())
                        .build());
                availableServiceButtons.add(button);
                inlineKeyboardMarkup.setKeyboard(availableServiceButtons);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(bundleLanguage.getValue(id, "search.result"));
                sendMessage.setChatId(id);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                sendBotMessageService.sendMessage(sendMessage);
                return true;
            } else {
                LOGGER.info("Not found service by {}", idService);
                user.setPositionMenu(PositionMenu.MENU_START);
                sendBotMessageService.sendMessage(String.valueOf(id), bundleLanguage.getValue(id, "search.searchId.notFound"));
                return false;
            }
        }
    }
}
