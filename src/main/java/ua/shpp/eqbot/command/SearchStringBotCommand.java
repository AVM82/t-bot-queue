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
import ua.shpp.eqbot.dto.BotCommandResultDto;
import ua.shpp.eqbot.dto.PrevPositionDTO;
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

@Component("searchstringBotCommand")
public class SearchStringBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchStringBotCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;
    private final Map<Long, Pair> pairMap = new HashMap<>();

    @Autowired
    public SearchStringBotCommand(SendBotMessageService sendBotMessageService,
                                  ServiceRepository serviceRepository,
                                  UserService userService,
                                  BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public BotCommandResultDto execute(Update update) {
        LOGGER.info("method execute search using the service name");
        long chatId;
        PrevPositionDTO prevPosition = new PrevPositionDTO();
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getFrom().getId();
        } else {
            chatId = update.getMessage().getChatId();
            prevPosition.setReceivedData("searchString/" + update.getMessage().getText());
            prevPosition.setPositionMenu(PositionMenu.SEARCH_USES_NAME_SERVICE);
            prevPosition.setTelegramId(chatId);
            userService.putPrevPosition(prevPosition);
        }
        UserDto user = userService.getDto(chatId);
        prevPosition = userService.getPrevPosition(chatId);
        if (user.getPositionMenu() != PositionMenu.SEARCH_USES_NAME_SERVICE) {
            user.setPositionMenu(PositionMenu.SEARCH_USES_NAME_SERVICE);
            sendBotMessageService.sendMessage(String.valueOf(chatId), bundleLanguage.getValue(chatId, "search.searchUsesNameService.text"));
            return true;
        } else {
            pairMap.computeIfAbsent(chatId, k -> new Pair(0));
            if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("searchString")) {
                pairMap.put(chatId, new Pair(userService.getPrevPosition(chatId).getPage()));
            }
            Paging paging = new Paging(serviceRepository);
            LOGGER.info("inner else find list use like");
            user.setPositionMenu(PositionMenu.SEARCH_USES_NAME_SERVICE);
            String likeString = "";
            if (update.hasMessage()) {
                likeString = update.getMessage().getText();
            }
            if (update.hasCallbackQuery()) {
                likeString = userService.getPrevPosition(chatId).getReceivedData().split("/")[1];
            }

            CallbackQuery callbackQuery = update.getCallbackQuery();

            if (callbackQuery != null && callbackQuery.getData().equals("next")) {
                LOGGER.info("next page  ========== >");
                pairMap.put(chatId, pairMap.get(chatId).increase());
                LOGGER.info("next page from {} to {}", pairMap.get(chatId).getFrom(), pairMap.get(chatId).getPagingSize());
            } else if (callbackQuery != null && callbackQuery.getData().equals("back")) {
                LOGGER.info("previous page  ========== >");
                pairMap.put(chatId, pairMap.get(chatId).decrease());
                LOGGER.info("previous page from {} to {}", pairMap.get(chatId).getFrom(), pairMap.get(chatId).getPagingSize());
            }

            Pair pair = pairMap.get(chatId);
            List<ServiceEntity> page = paging.getPage(pair.getFrom(), pair.getPagingSize(), likeString);

            if (callbackQuery != null && callbackQuery.getData().equals("exit")) {
                LOGGER.info("exit");
                pairMap.remove(chatId);
                user.setPositionMenu(PositionMenu.MENU_START);
                return true;
            }

            prevPosition.setPage(pair.getFrom());
            userService.putPrevPosition(prevPosition);
            if (!page.isEmpty()) {
                LOGGER.info("Found a list of services by description LIKE {} counts: {}", likeString, 777);
                pairMap.get(chatId).setLast(false);
                return fillListResulSelection(chatId, page);
            } else {
                LOGGER.info("No service were found for the string");
                List<ServiceEntity> previousPage = pairMap.get(chatId).getServiceEntities();
                pairMap.get(chatId).setLast(true);
                if (previousPage == null) {
                    user.setPositionMenu(PositionMenu.MENU_START);
                    pairMap.remove(chatId);
                    sendBotMessageService.sendMessage(String.valueOf(chatId), bundleLanguage.getValue(chatId, "search.by.like.last.page"));
                    return true;
                }
                return fillListResulSelection(chatId, previousPage);
            }
        }
    }


    boolean fillListResulSelection(long chatId, List<ServiceEntity> byDescriptionLike) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> availableServiceButtons = new ArrayList<>();

        byDescriptionLike.forEach(serviceEntity -> {
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(InlineKeyboardButton.builder()
                    .text(serviceEntity.getName() + " (ID: " + serviceEntity.getId() + ")")
                    .callbackData("service_info/" + serviceEntity.getId())
                    .build());
            availableServiceButtons.add(button);
        });

        List<InlineKeyboardButton> button = new ArrayList<>();
        if (pairMap.get(chatId).getFrom() != 0) {
            button.add(InlineKeyboardButton.builder()
                    .text("<<")
                    .callbackData("back")
                    .build());
        }
        button.add(InlineKeyboardButton.builder()
                .text("exit")
                .callbackData("exit").build());
        if (!pairMap.get(chatId).isLast()) {
            button.add(InlineKeyboardButton.builder()
                    .text(">>")
                    .callbackData("next")
                    .build());
        }
        availableServiceButtons.add(button);

        inlineKeyboardMarkup.setKeyboard(availableServiceButtons);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(bundleLanguage.getValue(chatId, "search.byCityName.listOfServices"));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
        pairMap.get(chatId).setServiceEntities(byDescriptionLike);
        return true;
    }
}
