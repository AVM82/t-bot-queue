package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.PrevPositionDTO;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;

import java.util.ArrayList;
import java.util.List;

@Component("searchidBotCommand")
public class SearchIdBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchIdBotCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public SearchIdBotCommand(SendBotMessageService sendBotMessageService,
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
            userService.putPrevPosition(new PrevPositionDTO(update.getMessage().getChatId(), PositionMenu.SEARCH_BY_ID, "searchId/" + update.getMessage().getText()));
        }

        UserDto user = userService.getDto(id);
        if (user.getPositionMenu() != PositionMenu.SEARCH_BY_ID) {
            user.setPositionMenu(PositionMenu.SEARCH_BY_ID);
            sendBotMessageService.sendMessage(String.valueOf(id), bundleLanguage.getValue(id, "search.searchId.text"));
            return true;
        } else {
            String idService;
            if (update.hasMessage()) {
                idService = update.getMessage().getText();
            } else {
                idService = update.getCallbackQuery().getData().split("/")[1];
            }
            ServiceEntity result = idService.matches("\\d+") ? serviceRepository.findFirstById(Long.valueOf(idService)) : null;
            if (result != null) {
                LOGGER.info("Found service by id");
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> availableServiceButtons = new ArrayList<>();
                List<InlineKeyboardButton> button = new ArrayList<>();
                button.add(InlineKeyboardButton.builder()
                        .text(result.getName() + " (ID: " + result.getId() + ")")
                        .callbackData("service_info/" + result.getId())
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
