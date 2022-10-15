package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.paging.Paging;
import ua.shpp.eqbot.paging.Pair;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchServiceBySimilarWordsCommander implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceBySimilarWordsCommander.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;
    private Map<Long, Pair> pairMap = new HashMap<>();

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
            if (!pairMap.containsKey(chatId)) {
                pairMap.put(chatId, new Pair(0));
            }
            Paging paging = new Paging(serviceRepository);
            LOGGER.info("inner else find list use like");
            user.setPositionMenu(PositionMenu.SEARCH_USES_NAME_SERVICE);
            String likeString = "";
            if (update.getMessage() != null) {
                likeString = update.getMessage().getText();
            }
//            List<ServiceEntity> byDescriptionLike =
//                    serviceRepository.findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(likeString, likeString);
            /*TODO extract this */
            CallbackQuery callbackQuery = update.getCallbackQuery();

            if (callbackQuery != null && callbackQuery.getData().equals("next")) {
                LOGGER.info("next page  ========== >");
                pairMap.put(chatId, pairMap.get(chatId).increase());
                LOGGER.info("next page from {} to {}", pairMap.get(chatId).getFrom(), pairMap.get(chatId).getSize());
            } else if (callbackQuery != null && callbackQuery.getData().equals("back")) {
                LOGGER.info("previous page  ========== >");
                pairMap.put(chatId, pairMap.get(chatId).decrease());
                LOGGER.info("previous page from {} to {}", pairMap.get(chatId).getFrom(), pairMap.get(chatId).getSize());
            }

            Pair pair = pairMap.get(chatId);
            List<ServiceEntity> page = paging.getPage(pair.getFrom(), pair.getSize());

            if (callbackQuery != null && callbackQuery.getData().equals("exit")) {
                LOGGER.info("exit");
                pairMap.remove(chatId);
                user.setPositionMenu(PositionMenu.MENU_START);
                return true;
            }

            if (!page.isEmpty()) {
                LOGGER.info("Found a list of services by description LIKE {} counts: {}", likeString, 777);
                return fillListResulSelection(chatId, page, bundleLanguage, sendBotMessageService);
            } else {
                LOGGER.info("No service were found for the string");
                sendBotMessageService.sendMessage(String.valueOf(chatId),
                        bundleLanguage.getValue(chatId, "search.searchId.notFound"));
                return false;
            }
        }
    }


    static boolean fillListResulSelection(long chatId, List<ServiceEntity> byDescriptionLike,
                                          BundleLanguage bundleLanguage,
                                          SendBotMessageService sendBotMessageService) {
        int size = byDescriptionLike.size();
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

        List<InlineKeyboardButton> button = new ArrayList<>();
        button.add(InlineKeyboardButton.builder()
                .text("<<")
                .callbackData("back")
                .build());
        button.add(InlineKeyboardButton.builder()
                .text("exit")
                .callbackData("exit").build());
        button.add(InlineKeyboardButton.builder()
                .text(">>")
                .callbackData("next").build());
        availableServiceButtons.add(button);

        inlineKeyboardMarkup.setKeyboard(availableServiceButtons);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(bundleLanguage.getValue(chatId, "search.byCityName.listOfServices"));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);

        return false;
    }
}