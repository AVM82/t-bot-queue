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
public class SearchServiceBySimilarWordsCommander implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceBySimilarWordsCommander.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public SearchServiceBySimilarWordsCommander(SendBotMessageService sendBotMessageService,
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
        LOGGER.info("method execute search using the service name");
        long chatId;
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getFrom().getId();
        } else {
            chatId = update.getMessage().getChatId();
        }
        UserDto user = userService.getDto(chatId);

        if (user.getPositionMenu() != PositionMenu.SEARCH_USES_NAME_SERVICE) {
            user.setPositionMenu(PositionMenu.SEARCH_USES_NAME_SERVICE);
            sendBotMessageService.sendMessage(String.valueOf(chatId), bundleLanguage.getValue(chatId, "search.searchUsesNameService.text"));
            return true;
        } else {
            LOGGER.info("inner else find list use like");
            user.setPositionMenu(PositionMenu.SEARCH_BY_NAME);
            String likeString = update.getMessage().getText();
            List<ServiceEntity> byDescriptionLike = serviceRepository.findByDescriptionContaining(likeString);
            if (!byDescriptionLike.isEmpty()) {
                LOGGER.info("Found a list of services by description LIKE {} counts: {}", likeString, byDescriptionLike.size());
                return fillListResulSelection(chatId, byDescriptionLike, bundleLanguage, sendBotMessageService);
            } else {
                LOGGER.info("No service were found for the string");
                sendBotMessageService.sendMessage(String.valueOf(chatId),
                        bundleLanguage.getValue(chatId, "search.searchId.notFound"));
                return false;
            }
        }
    }

    static boolean fillListResulSelection(long chatId, List<ServiceEntity> byDescriptionLike, BundleLanguage bundleLanguage, SendBotMessageService sendBotMessageService) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> availableServiceButtons = new ArrayList<>();

        byDescriptionLike.forEach(serviceEntity -> {
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(InlineKeyboardButton.builder()
                    .text(serviceEntity.getName())
                    .callbackData(String.valueOf(serviceEntity.getId()))
                    .build());
            availableServiceButtons.add(button);
        });
        inlineKeyboardMarkup.setKeyboard(availableServiceButtons);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(bundleLanguage.getValue(chatId, "command.search_service.messages.list_of_services"));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
        return true;
    }
}
