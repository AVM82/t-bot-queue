package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.BotCommandResultDto;
import ua.shpp.eqbot.dto.PrevPositionDTO;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

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
    public BotCommandResultDto execute(Update update) {
        BotCommandResultDto resultDto = new BotCommandResultDto();
        long id;
        PrevPositionDTO prevPosition = new PrevPositionDTO();
        prevPosition.setPositionMenu(SEARCH_BY_CITY_NAME);
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else {
            id = update.getMessage().getChatId();
            prevPosition.setReceivedData("searchCity/" + update.getMessage().getText());
            prevPosition.setTelegramId(id);
            userService.putPrevPosition(prevPosition);
        }

        UserDto user = userService.getDto(id);

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
                    .text(bundleLanguage.getValue(id, "search.byCityName.anotherCity"))
                    .callbackData("another_city")
                    .build());
            citySelection.add(city);
            inlineKeyboardMarkup.setKeyboard(citySelection);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(id);
            sendMessage.setText(bundleLanguage.getValue(id, "search.byCityName.cityToSearch"));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendBotMessageService.sendMessage(sendMessage);
        } else {
            String city;
            if (update.hasCallbackQuery()) {
                if (update.getCallbackQuery().getData().equals("another_city")) {
                    sendBotMessageService.sendMessage(String.valueOf(id), bundleLanguage.getValue(id, "search.byCityName.enterTheCity"));
                    return resultDto.setDone(true);
                } else if (update.getCallbackQuery().getData().equals(user.getCity())) {
                    city = update.getCallbackQuery().getData();
                    prevPosition.setReceivedData("searchCity/" + city);
                    prevPosition.setTelegramId(id);
                    userService.putPrevPosition(prevPosition);
                } else {
                    city = userService.getPrevPosition(id).getReceivedData().split("/")[1];
                }
            } else {
                city = update.getMessage().getText();
            }

            List<ProviderEntity> providerEntityByCityList = providerRepository.findAllByProviderCity(city);
            if (providerEntityByCityList.isEmpty()) {
                LOGGER.info("No service providers were found for the user's city of registration");
                sendBotMessageService.sendMessage(String.valueOf(id),
                        bundleLanguage.getValue(id, "search.byCityName.providerNotFound"));
                return resultDto.setDone(false);
            }

            List<Long> telegramIdProviderByCityList = providerEntityByCityList.stream()
                    .map(ProviderEntity::getTelegramId)
                    .collect(Collectors.toList());

            List<ServiceEntity> serviceEntityByCityList = serviceRepository.findAllByTelegramIdIn(telegramIdProviderByCityList);
            if (serviceEntityByCityList.isEmpty()) {
                LOGGER.info("No services were found for the user's city of registration");
                sendBotMessageService.sendMessage(String.valueOf(id),
                        bundleLanguage.getValue(id, "search.byCityName.serviceNotFound"));
                return resultDto.setDone(false);
            }

            LOGGER.info("Found a list of services by city of user registration");

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> availableServiceButtons = new ArrayList<>();

            serviceEntityByCityList.forEach(serviceEntity -> {
                List<InlineKeyboardButton> button = new ArrayList<>();
                button.add(InlineKeyboardButton.builder()
                        .text(serviceEntity.getName() + " (ID: " + serviceEntity.getId() + ")")
                        .callbackData("service_info/" + serviceEntity.getId())
                        .build());
                availableServiceButtons.add(button);
            });

            inlineKeyboardMarkup.setKeyboard(availableServiceButtons);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(id);
            sendMessage.setText(String.format("%s %s", bundleLanguage.getValue(id, "search.byCityName.listOfServices"), city));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendBotMessageService.sendMessage(sendMessage);

        }
        return resultDto.setDone(true);
    }
}
