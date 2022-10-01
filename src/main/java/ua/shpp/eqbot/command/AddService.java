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
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.repository.ProvideRepository;
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

    private final ProvideRepository provideRepository;

    @Autowired
    public AddService(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository, ImageService imageService, ProvideRepository provideRepository, UserService userService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.imageService = imageService;
        this.provideRepository = provideRepository;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
    }

    public void addService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setIdTelegram(service.getIdTelegram())
                .setName(service.getName())
                .setDescription(service.getDescription())
                .setAvatar(service.getAvatar());
        serviceRepository.save(serviceEntity);
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

        if (provideRepository.findById(id).isPresent())/*providerRepository.findById(update.getMessage().getChatId())*/ {
            ServiceDTO newService;
            if (!update.hasMessage()) {
                Long idTelegram = update.getCallbackQuery().getFrom().getId();
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(ADD_SERVICE_MESSAGE).build());
                LOGGER.info("Add new service.");
                return false;
            }
            newService = ServiceCache.findBy(update.getMessage().getChatId());
            Long idTelegram = update.getMessage().getChat().getId();
            UserDto user = userService.getDto(idTelegram);
            if (newService == null) {
                if (checkIfServiceExists(update.getMessage().getText().trim(), idTelegram)) {
                    createService(update);
                    return false;
                }
                newService = new ServiceDTO();
                newService.setIdTelegram(user.getIdTelegram()).setName(update.getMessage().getText().trim());
                LOGGER.info("i want to ask name service");
                String message = bundleLanguage.getValue(update.getMessage().getChatId(), "add_desc_and_avatar");
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram)
                        .text(message).build());
                ServiceCache.add(newService);
            } else {
                LOGGER.info("service present");
                newService = ServiceCache.findBy(update.getMessage().getChatId());
                addingDescriptionAndAvatar(update.getMessage(), newService);
                ServiceCache.remove(newService);
                user.setPositionMenu(MENU_START);
                String message = bundleLanguage.getValue(update.getMessage().getChatId(), "service_added");
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram)
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
            if (update.getCallbackQuery() != null) {
                sendBotMessageService.setReplyMarkup(update.getCallbackQuery().getFrom().getId().toString(), markup);
            }
            ServiceCache.justRegistrated = true;
        }
        return false;
    }

    private void createService(Update update) {
        Long idTelegram;
        if (update.getMessage() != null) {
            idTelegram = update.getMessage().getChatId();
        } else {
            idTelegram = update.getCallbackQuery().getFrom().getId();
        }
        String message = bundleLanguage.getValue(idTelegram, "input_name_service");
        sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(message).build());
        LOGGER.info("Add new service.");
    }

    private boolean checkIfServiceExists(String name, Long idTelegram) {
        boolean result = serviceRepository.getFirstByNameAndId(name, idTelegram) != null;
        if (result) {
            String message = bundleLanguage.getValue(idTelegram, "service_exist");
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram)
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
