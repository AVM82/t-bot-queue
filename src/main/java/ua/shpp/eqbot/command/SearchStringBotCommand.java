package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ua.shpp.eqbot.dto.PrevPositionDTO;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.SendBotMessageServiceImpl;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;

@Component("searchstringBotCommand")
public class SearchStringBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchStringBotCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;
//    private final Map<Long, Pair> pairMap = new HashMap<>();

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
    public boolean execute(Update update) {
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
                prevPosition.setPage(prevPosition.getPage() + 1);
                //               LOGGER.info("next page from {} to {}", pairMap.get(chatId).getFrom(), pairMap.get(chatId).getPagingSize());
            } else if (callbackQuery != null && callbackQuery.getData().equals("back")) {
                LOGGER.info("previous page  ========== >");
                prevPosition.setPage(prevPosition.getPage() - 1);
            }
            userService.putPrevPosition(prevPosition);
            Pageable firstPageWithTwoElements = PageRequest.of(prevPosition.getPage(), SendBotMessageServiceImpl.PAGE_SIZE);
            Page<ServiceEntity> page = serviceRepository.findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(likeString, likeString, firstPageWithTwoElements);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(sendBotMessageService.createPageableKeyboard(page, prevPosition, bundleLanguage));
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(bundleLanguage.getValue(chatId, "search.byCityName.listOfServices"));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendBotMessageService.sendMessage(sendMessage);
        }
        return true;
    }


}
