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
import ua.shpp.eqbot.model.ProviderDto;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionRegistrationProvider;
import ua.shpp.eqbot.stage.PositionRegistrationService;

import java.util.ArrayList;
import java.util.List;

import static ua.shpp.eqbot.stage.PositionMenu.MENU_START;

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
    private boolean hasSuchName = false;

    @Autowired
    public AddService(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository, ImageService imageService, ProvideRepository provideRepository, UserService userService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.imageService = imageService;
        this.provideRepository = provideRepository;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        boolean isRegistration =false;
        Long id;
        if (update.hasCallbackQuery())
            id = update.getCallbackQuery().getFrom().getId();
        else if (update.hasMessage())
            id = update.getMessage().getChatId();
        else
            return false;
        ServiceDTO serviceDTO = ServiceCache.findBy(id);
        LOGGER.info("i try register new service");
        if (serviceDTO == null) {
            ServiceCache.add(generateServiceFromMessage(id));
            assert id != null;
            createMessage(id, "new_service");
            createMessage(id, "input_name_service");
        } else {
            switch (serviceDTO.getPositionRegistrationService()) {
                case INPUT_SERVICE_NAME:
                    LOGGER.info("new service INPUT_USERNAME with message text {}", update.getMessage().getText());
                    if (update.getMessage() != null && !update.getMessage().isCommand()) {
                        ServiceCache.add(serviceDTO.setName(update.getMessage().getText())
                                .setPositionRegistrationService(PositionRegistrationService.INPUT_PICTURE));
                        createMessage(id, "add_desc_and_avatar");
                    }
                    break;
                case INPUT_PICTURE:
                    LOGGER.info("new service INPUT_CITY with message text {}", update.getMessage().getText());
                    if (update.getMessage() != null && !update.getMessage().isCommand()) {
                        ServiceCache.add(addingDescriptionAndAvatar(update.getMessage(), serviceDTO)
                                .setPositionRegistrationService(PositionRegistrationService.START_WORK_IN_MONDAY));
                        createMessage(id, "beginning_of_work","monday","format");
                    }
                    break;
                case START_WORK_IN_MONDAY:
                    break;
                case END_WORK_IN_MONDAY:
                    break;
                case START_WORK_IN_TUESDAY:
                    break;
                case END_WORK_IN_TUESDAY:
                    break;
                case START_WORK_IN_WEDNESDAY:
                    break;
                case END_WORK_IN_WEDNESDAY:
                    break;
                case START_WORK_IN_THURSDAY:
                    break;
                case END_WORK_IN_THURSDAY:
                    break;
                case START_WORK_IN_FRIDAY:
                    break;
                case END_WORK_IN_FRIDAY:
                    break;
                case START_WORK_IN_SATURDAY:
                    break;
                case END_WORK_IN_SATURDAY:
                    break;
                case START_WORK_IN_SUNDAY:
                    break;
                case END_WORK_IN_SUNDAY:
                    break;
                case TIME_BETWEEN_CLIENTS:
                    break;
                default:
                    //do nothing
                    break;
            }
        }
        return isRegistration;



     /*   ServiceDTO newService;
        if (!update.hasMessage()) {
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(id).text(ADD_SERVICE_MESSAGE).build());
            LOGGER.info("Add new service.");
            return false;
        }
        newService = ServiceCache.findBy(update.getMessage().getChatId());
        UserDto user = userService.getDto(id);
        if (newService == null) {
            if (checkIfServiceExists(update.getMessage().getText().trim(), id)) {
                createService(update);
                return false;
            }
            newService = new ServiceDTO();
            newService.setIdTelegram(user.getIdTelegram()).setName(update.getMessage().getText().trim());
            LOGGER.info("i want to ask name service");
            String message = bundleLanguage.getValue(update.getMessage().getChatId(), "add_desc_and_avatar");
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(id)
                    .text(message).build());
            ServiceCache.add(newService);
        } else {
            LOGGER.info("service present");
            newService = ServiceCache.findBy(update.getMessage().getChatId());
            addingDescriptionAndAvatar(update.getMessage(), newService);
            ServiceCache.remove(newService);
            user.setPositionMenu(MENU_START);
            String message = bundleLanguage.getValue(update.getMessage().getChatId(), "service_added");
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(id)
                    .text(message).build());
        }
        return false;*/
    }

    private ServiceDTO generateServiceFromMessage(Long id) {
        ServiceDTO dto = new ServiceDTO();
        dto.setIdTelegram(id)
                .setPositionRegistrationService(PositionRegistrationService.INPUT_SERVICE_NAME);
        return dto;
    }

    private void createService(Update update) {
        hasSuchName = false;
        Long idTelegram;
        if (update.hasMessage()) {
            idTelegram = update.getMessage().getChatId();
        } else {
            idTelegram = update.getCallbackQuery().getFrom().getId();
        }
        String message = bundleLanguage.getValue(idTelegram, "input_name_service");
        sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(message).build());
        LOGGER.info("Add new service.");
    }

    private boolean checkIfServiceExists(String name, Long idTelegram) {
        boolean result = serviceRepository.getFirstByNameAndAndIdTelegram(name, idTelegram) != null;
        if (result) {
            String message = bundleLanguage.getValue(idTelegram, "service_exist");
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram)
                    .text(message).build());
            hasSuchName = true;
        }
        return result;
    }

    public void addService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setIdTelegram(service.getIdTelegram())
                .setName(service.getName())
                .setDescription(service.getDescription())
                .setAvatar(service.getAvatar());
        serviceRepository.save(serviceEntity);
    }

    private ServiceDTO addingDescriptionAndAvatar(Message message, ServiceDTO serviceDTO) {
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
        //addService(serviceDTO);
        return serviceDTO;
    }

    private boolean changeFormatTime(String time, Long id){
        if (!(time.matches("\\d{1,2}[:]{1}\\d{1,2}")))
            return true;
        else {
            createMessage(id,"");
            return false;
        }
    }

    private void createMessage(Long id, String text) {
        sendBotMessageService.sendMessage(SendMessage.builder()
                .text(bundleLanguage.getValue(id, text))
                .chatId(id)
                .build());
    }

    private void createMessage(Long id, String text1, String text2) {
        sendBotMessageService.sendMessage(SendMessage.builder()
                .text(bundleLanguage.getValue(id, text1)+ " "+
                        bundleLanguage.getValue(id, text2))
                .chatId(id)
                .build());
    }

    private void createMessage(Long id, String text1, String text2, String text3) {
        sendBotMessageService.sendMessage(SendMessage.builder()
                .text(bundleLanguage.getValue(id, text1)+ " "+
                        bundleLanguage.getValue(id, text2)+ " "+
                        bundleLanguage.getValue(id, text3))
                .chatId(id)
                .build());
    }
}
