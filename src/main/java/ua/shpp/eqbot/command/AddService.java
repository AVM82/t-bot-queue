package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.cache.BotUserCache;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.model.PositionRegistration;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

import static ua.shpp.eqbot.model.PositionMenu.MENU_CREATE_SERVICE;
import static ua.shpp.eqbot.model.PositionMenu.MENU_START;

@Component
public class AddService implements Command {

    private static final Logger log = LoggerFactory.getLogger(AddService.class);
    private final SendBotMessageService sendBotMessageService;

    public static final String ADD_SERVICE_MESSAGE = "Введіть назву послуги:\n";
    private final ServiceRepository serviceRepository;
    private final ImageService imageService;

    @Autowired
    public AddService(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository, ImageService imageService) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.imageService = imageService;
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
        UserDto user = null;
        ServiceDTO newService;
        if (!update.hasMessage()) {
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
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram)
                    .text("Додайте опис та зображення для сервісу").build());
            ServiceCache.add(newService);
        }else {
            newService = ServiceCache.findBy(update.getMessage().getChatId());
            addingDescriptionAndAvatar(update, newService);
            user.setPositionMenu(MENU_START);
            ServiceCache.remove(newService);
        }

        return false;
    }

    private boolean addingDescriptionAndAvatar(Update update, ServiceDTO serviceDTO) {

        if (update.getMessage().hasPhoto()) {
            byte[] imageArray = imageService.getArrayOfLogo(update.getMessage().getPhoto());
            serviceDTO.setAvatar(imageArray);
            log.warn("Adding photo");
            serviceDTO.setDescription(update.getMessage().getCaption());
            log.warn("Adding text");
        }
        if (update.getMessage().hasText()) {
            serviceDTO.setDescription(update.getMessage().getText());
            log.warn("Adding text");
        }
        addService(serviceDTO);

        return true;
    }


    public void askForAvatarAndDescription(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttonCreate = new ArrayList<>();
        buttonCreate.add(InlineKeyboardButton.builder()
                .text("Додати зображення та опис")
                .callbackData("create_service")
                .build());
        keyboard.add(buttonCreate);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Успішно!");
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);

    }
}
