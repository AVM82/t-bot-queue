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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.PrevPositionDTO;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.SendBotMessageServiceImpl;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ua.shpp.eqbot.stage.PositionMenu.SEARCH_BY_CITY_NAME;

@Component("searchBotCommand")
public class SearchServiceBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceBotCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final ProviderRepository providerRepository;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public SearchServiceBotCommand(SendBotMessageService sendBotMessageService,
                                   ServiceRepository serviceRepository,
                                   ProviderRepository providerRepository,
                                   UserService userService,
                                   BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.providerRepository = providerRepository;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {

        long telegramId;


        if (update.hasCallbackQuery()) {
            telegramId = update.getCallbackQuery().getFrom().getId();
        } else {
            telegramId = update.getMessage().getChatId();
        }
        PrevPositionDTO prevPosition = userService.getPrevPosition(telegramId);
        if (prevPosition == null) {
            prevPosition = new PrevPositionDTO();
        }
        prevPosition.setPositionMenu(SEARCH_BY_CITY_NAME);
        prevPosition.setTelegramId(telegramId);
        userService.putPrevPosition(prevPosition);
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null && callbackQuery.getData().equals("next")) {
            LOGGER.info("next page  ========== >");
            prevPosition.setPage(prevPosition.getPage() + 1);
        } else if (callbackQuery != null && callbackQuery.getData().equals("back")) {
            LOGGER.info("previous page  ========== >");
            prevPosition.setPage(prevPosition.getPage() - 1);
        }

        UserDto user = userService.getDto(telegramId);

        if (!user.getPositionMenu().equals(SEARCH_BY_CITY_NAME)) {
            LOGGER.info("Choosing a city to search for a service");
            user.setPositionMenu(SEARCH_BY_CITY_NAME);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> citySelection = new ArrayList<>();
            List<InlineKeyboardButton> city = new ArrayList<>();
            city.add(InlineKeyboardButton.builder()
                    .text(user.getCity())
                    .callbackData(user.getCity())
                    .build());
            city.add(InlineKeyboardButton.builder()
                    .text(bundleLanguage.getValue(telegramId, "search.byCityName.anotherCity"))
                    .callbackData("another_city")
                    .build());
            citySelection.add(city);
            inlineKeyboardMarkup.setKeyboard(citySelection);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(telegramId);
            sendMessage.setText(Icon.MAG.get() + " " + bundleLanguage.getValue(telegramId, "search.byCityName.cityToSearch"));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendBotMessageService.sendMessage(sendMessage);
        } else {
            String city;
            if (update.hasCallbackQuery()) {
                if (update.getCallbackQuery().getData().equals("another_city")) {
                    sendBotMessageService.sendMessage(String.valueOf(telegramId), Icon.HOUSE_BUILDINGS.get() + " " +  bundleLanguage.getValue(telegramId, "search.byCityName.enterTheCity"));
                    return true;
                } else if (update.getCallbackQuery().getData().equals(user.getCity())) {
                    city = update.getCallbackQuery().getData();
                    prevPosition.setReceivedData("searchCity/" + city);
                    prevPosition.setTelegramId(telegramId);
                    userService.putPrevPosition(prevPosition);
                } else {
                    city = userService.getPrevPosition(telegramId).getReceivedData().split("/")[1];
                }
            } else {
                city = update.getMessage().getText();
                prevPosition.setReceivedData("searchCity/" + update.getMessage().getText());
            }
            List<ProviderEntity> providerEntityByCityList = providerRepository.findAllByProviderCity(city);
            if (providerEntityByCityList.isEmpty()) {
                LOGGER.info("No service providers were found for the user's city of registration");
                sendBotMessageService.sendMessage(String.valueOf(telegramId),
                        bundleLanguage.getValue(telegramId, "search.byCityName.providerNotFound"));
                return false;
            }

            return generateInlineKeyboard(telegramId, city, prevPosition);
        }
        return true;
    }

    private boolean generateInlineKeyboard(long telegramId, String city, PrevPositionDTO prevPosition) {
        List<ProviderEntity> providerEntityByCityList = providerRepository.findAllByProviderCity(city);
        if (providerEntityByCityList.isEmpty()) {
            LOGGER.info("No service providers were found for the user's city of registration");
            sendBotMessageService.sendMessage(String.valueOf(telegramId),
                    bundleLanguage.getValue(telegramId, "search.byCityName.providerNotFound"));
            return false;
        }

        List<Long> telegramIdProviderByCityList = providerEntityByCityList.stream()
                .map(ProviderEntity::getTelegramId)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(prevPosition.getPage(), SendBotMessageServiceImpl.PAGE_SIZE);
        Page<ServiceEntity> serviceEntityByCityList = serviceRepository.findAllByTelegramIdIn(telegramIdProviderByCityList, pageable);
        if (serviceEntityByCityList.isEmpty()) {
            LOGGER.info("No services were found for the user's city of registration");
            sendBotMessageService.sendMessage(String.valueOf(telegramId),
                    bundleLanguage.getValue(telegramId, "search.byCityName.serviceNotFound"));
            return false;
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(sendBotMessageService.createPageableKeyboard(serviceEntityByCityList, prevPosition, bundleLanguage));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramId);
        sendMessage.setText(Icon.PAGE_WITH_CURL.get() + " " + bundleLanguage.getValue(telegramId, "search.byCityName.listOfServices"));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
        return true;
    }
}
