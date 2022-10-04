package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.dto.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static ua.shpp.eqbot.model.PositionMenu.MENU_START;

@Component
public class AddService implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddService.class);
    private final SendBotMessageService sendBotMessageService;
    public static final String ADD_SERVICE_MESSAGE = "input_name_service";
    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final BundleLanguage bundleLanguage;

    private final ProviderRepository providerRepository;

    @Autowired
    public AddService(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository, ImageService imageService, ProviderRepository providerRepository, UserService userService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.imageService = imageService;
        this.providerRepository = providerRepository;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
    }

    public ServiceEntity addService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setTelegramId(service.getTelegramId())
                .setName(service.getName())
                .setDescription(service.getDescription())
                .setAvatar(service.getAvatar());
       return serviceRepository.save(serviceEntity);
    }


    @Override
    public boolean execute(Update update) {
        long id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else {
            /*TODO if message null check it*/
            id = update.getMessage().getChatId();
        }

        if (providerRepository.findByTelegramId(id) != null)/*providerRepository.findById(update.getMessage().getChatId())*/ {
            ServiceDTO newService;
            if (!update.hasMessage()) {
                Long telegramId = update.getCallbackQuery().getFrom().getId();
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(telegramId).text(ADD_SERVICE_MESSAGE).build());
                LOGGER.info("Add new service.");
                return false;
            }
            newService = ServiceCache.findBy(update.getMessage().getChatId());
            Long telegramId = update.getMessage().getChat().getId();
            UserDto user = userService.getDto(telegramId);
            if (newService == null) {
                if (checkIfServiceExists(update.getMessage().getText().trim(), telegramId)) {
                    createService(update);
                    return false;
                }
                newService = new ServiceDTO();
                newService.setTelegramId(user.getTelegramId()).setName(update.getMessage().getText().trim());
                LOGGER.info("i want to ask name service");
                String message = bundleLanguage.getValue(update.getMessage().getChatId(), "add_desc_and_avatar");
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(telegramId)
                        .text(message).build());
                ServiceCache.add(newService);
            } else {
                LOGGER.info("service present");
                newService = ServiceCache.findBy(update.getMessage().getChatId());
                addingDescriptionAndAvatar(update.getMessage(), newService);
                ServiceCache.remove(newService);
                user.setPositionMenu(MENU_START);
                String message = bundleLanguage.getValue(update.getMessage().getChatId(), "service_added");
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(telegramId)
                        .text(message).build());
            }
        } else {
            LOGGER.info("provider present");
            var markup = new ReplyKeyboardMarkup();
            var keyboardRows = new ArrayList<KeyboardRow>();
            KeyboardRow registrationNewProvider = new KeyboardRow();
            registrationNewProvider.add("Реєстрація нового провайдера");
            keyboardRows.add(registrationNewProvider);
            markup.setKeyboard(keyboardRows);
            markup.setResizeKeyboard(true);
            LOGGER.info("Didn't find provider with such id");
            sendBotMessageService.setReplyMarkup(update.getCallbackQuery().getFrom().getId().toString(), markup);
            ServiceCache.justRegistrated = true;
        }
        return false;
    }

    private void createService(Update update) {
        Long telegramId;
        if (update.getMessage() != null) {
            telegramId = update.getMessage().getChatId();
        } else {
            telegramId = update.getCallbackQuery().getFrom().getId();
        }
        String message = bundleLanguage.getValue(telegramId, "input_name_service");
        sendBotMessageService.sendMessage(SendMessage.builder().chatId(telegramId).text(message).build());
        LOGGER.info("Add new service.");
    }

    private boolean checkIfServiceExists(String name, Long telegramId) {
        boolean result = serviceRepository.getFirstByNameAndAndTelegramId(name, telegramId) != null;
        if (result) {
            String message = bundleLanguage.getValue(telegramId, "service_exist");
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(telegramId)
                    .text(message).build());
        }
        return result;
    }

    private void addingDescriptionAndAvatar(Message message, ServiceDTO serviceDTO) {
        LOGGER.info("i want to addingDescriptionAndAvatar ");
        if (message.hasPhoto()) {
            List<PhotoSize> photos = message.getPhoto();
            byte[] imageArray = imageService.getArrayOfLogo(photos);
            serviceDTO.setAvatar(imageArray);
            LOGGER.info("Adding image to DB");
            imageService.sendBigImageToAWS(photos, message.getChatId().toString() + "/" + serviceDTO.getName());
            serviceDTO.setDescription(message.getCaption());
            LOGGER.info("Adding description");
        }
        if (message.hasText()) {
            serviceDTO.setDescription(message.getText());
            LOGGER.info("Adding description");
        }
        addService(serviceDTO);
    }
}
