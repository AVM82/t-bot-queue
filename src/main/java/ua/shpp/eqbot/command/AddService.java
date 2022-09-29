package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.shpp.eqbot.cache.BotUserCache;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

import static ua.shpp.eqbot.model.PositionMenu.MENU_START;

@Component
public class AddService implements Command {

    private static final Logger log = LoggerFactory.getLogger(AddService.class);
    private final SendBotMessageService sendBotMessageService;
    public static final String ADD_SERVICE_MESSAGE = "input_name_service";
    private final ServiceRepository serviceRepository;

    private final ImageService imageService;

    ProvideRepository provideRepository;

    @Autowired
    public AddService(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository, ImageService imageService, ProvideRepository provideRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.imageService = imageService;
        this.provideRepository = provideRepository;
    }

    public void addService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setId_telegram(service.getId_telegram())
                .setName(service.getName())
                .setDescription(service.getDescription())
                .setAvatar(service.getAvatar());
        serviceRepository.save(serviceEntity);
    }


    @Override
    public boolean execute(Update update) {
        log.info("add service method execute start {}", update.getMessage().getText());
        long id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else {
            id = update.getMessage().getChatId();
        }
        log.info("add service method execute callbackquery {}", update.getMessage().getText());
        if (provideRepository.findById_telegram(id) != null)/*providerRepository.findById(update.getMessage().getChatId())*/ {
            UserDto user = null;
            ServiceDTO newService;
            if (!update.hasMessage() ) {
                log.info("update don't have message");
                Long idTelegram = update.getCallbackQuery().getFrom().getId();
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(ADD_SERVICE_MESSAGE).build());
                log.info("Add new service.");
                return false;
            }
            newService = ServiceCache.findBy(update.getMessage().getChatId());
            Long idTelegram = update.getMessage().getChat().getId();
            user = BotUserCache.findBy(idTelegram);
            if (newService == null) {
                newService = new ServiceDTO();
                newService.setId_telegram(user.getId_telegram()).setName(update.getMessage().getText().trim());
                log.info("i want to ask name service");
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram)
                        .text("Додайте опис та зображення для сервісу").build());
                ServiceCache.add(newService);
            } else {
                log.info("service present");
                newService = ServiceCache.findBy(update.getMessage().getChatId());
                addingDescriptionAndAvatar(update.getMessage(), newService);
                ServiceCache.remove(newService);
                user.setPositionMenu(MENU_START);
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram)
                        .text("Сервіс успішно додано").build());
            }
        } else {
            log.info("provider present");
            var markup = new ReplyKeyboardMarkup();
            var keyboardRows = new ArrayList<KeyboardRow>();
            KeyboardRow registrationNewProvider = new KeyboardRow();
            registrationNewProvider.add("Реєстрація нового провайдера");
            keyboardRows.add(registrationNewProvider);
            markup.setKeyboard(keyboardRows);
            markup.setResizeKeyboard(true);
            log.info("Didn't find provider with such id");
            sendBotMessageService.setReplyMarkup(update.getCallbackQuery().getFrom().getId().toString(), markup);
            ServiceCache.justRegistrated=true;
        }


        return false;
    }

    private void addingDescriptionAndAvatar(Message message, ServiceDTO serviceDTO) {
        log.info("i want to addingDescriptionAndAvatar {}", message.getText());
        if (message.hasPhoto()) {
            List<PhotoSize> photos = message.getPhoto();
            byte[] imageArray = imageService.getArrayOfLogo(photos);
            serviceDTO.setAvatar(imageArray);
            log.info("Adding image to DB");
            imageService.sendBigImageToAWS(photos, message.getChatId().toString() + "/" + serviceDTO.getName());
            serviceDTO.setDescription(message.getCaption());
            log.info("Adding description");
        }
        if (message.hasText()) {
            serviceDTO.setDescription(message.getText());
            log.info("Adding description");
        }
        addService(serviceDTO);
    }

}
