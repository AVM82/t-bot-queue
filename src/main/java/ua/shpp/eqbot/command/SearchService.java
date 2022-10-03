package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchService implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final ProviderRepository providerRepository;
    private final UserService userService;
    private final BundleLanguage bundleLanguage;

    @Autowired
    public SearchService(SendBotMessageService sendBotMessageService,
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
        long id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else {
            id = update.getMessage().getChatId();
        }

        UserDto user = userService.getDto(id);
        String city = user.getCity();
        List<ProviderEntity> providerEntityByCityList = providerRepository.findAllByCity(city);
        if (providerEntityByCityList.isEmpty()) {
            LOGGER.info("No service providers were found for the user's city of registration");
            sendBotMessageService.sendMessage(String.valueOf(id),
                    bundleLanguage.getValue(id, "command.search_service.messages.provider_not_found"));
            return false;
        }

        List<Long> idTelegramProviderByCityList = providerEntityByCityList.stream()
                .map(ProviderEntity::getIdTelegram)
                .collect(Collectors.toList());

        List<ServiceEntity> serviceEntityByCityList = serviceRepository.findAllByIdTelegramIn(idTelegramProviderByCityList);
        if (serviceEntityByCityList.isEmpty()) {
            LOGGER.info("No services were found for the user's city of registration");
            sendBotMessageService.sendMessage(String.valueOf(id),
                    bundleLanguage.getValue(id, "command.search_service.messages.service_not_found"));
            return false;
        }

        LOGGER.info("Found a list of services by city of user registration");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> availableServiceButtons = new ArrayList<>();

        serviceEntityByCityList.forEach(serviceEntity -> {
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(InlineKeyboardButton.builder()
                    .text(serviceEntity.getName())
                    .callbackData(String.valueOf(serviceEntity.getId()))
                    .build());
            availableServiceButtons.add(button);
        });

        inlineKeyboardMarkup.setKeyboard(availableServiceButtons);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(bundleLanguage.getValue(id, "command.search_service.messages.list_of_services"));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);

        return true;
    }
}
