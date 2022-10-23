package ua.shpp.eqbot.command.registrationservice;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.ServiceCache;
import ua.shpp.eqbot.command.BotCommand;
import ua.shpp.eqbot.dto.ServiceDTO;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.stage.PositionRegistrationService;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.List;

@Component("addBotCommand")
public class AddServiceBotCommand implements BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddServiceBotCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final ImageService imageService;
    private final BundleLanguage bundleLanguage;
    private final ModelMapper modelMapper = new ModelMapper();

    private static final String TEXT = "work_day_schedule";
    private static final String FORMAT = "format";

    @Autowired
    public AddServiceBotCommand(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository,
                                ImageService imageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.imageService = imageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        boolean isRegistration = false;
        Long id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else if (update.hasMessage()) {
            id = update.getMessage().getChatId();
        } else {
            return false;
        }
        ServiceDTO serviceDTO = ServiceCache.findBy(id);
        LOGGER.info("i try register new service");
        if (serviceDTO == null) {
            ServiceCache.add(generateServiceFromMessage(id));
            assert id != null;
            createMessage(id, "new_service");
            createMessage(id, "input_name_service");
        } else {
            if (update.getMessage() != null && !update.getMessage().isCommand()) {
                switch (serviceDTO.getPositionRegistrationService()) {
                    case INPUT_SERVICE_NAME:
                        LOGGER.info("new service INPUT_USERNAME with message text {}", update.getMessage().getText());
                        ServiceCache.add(serviceDTO.setName(update.getMessage().getText())
                                .setPositionRegistrationService(PositionRegistrationService.INPUT_PICTURE));
                        sendBotMessageService.sendMessage(String.valueOf(id), Icon.PENCIL.get() + " "
                                + bundleLanguage.getValue(id, "add_desc_and_avatar")
                                + " " + Icon.GALLERY.get());
                        break;
                    case INPUT_PICTURE:
                        LOGGER.info("new service INPUT_CITY with message text {}", update.getMessage().getText());
                        ServiceCache.add(addingDescriptionAndAvatar(update.getMessage(), serviceDTO)
                                .setPositionRegistrationService(PositionRegistrationService.MONDAY_WORKING_HOURS));
                        createMessage(id, new String[]{TEXT, "monday", FORMAT});
                        break;
                    case MONDAY_WORKING_HOURS:
                        LOGGER.info("new service MONDAY_WORKING_HOURS with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setMondayWorkingHours(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.TUESDAY_WORKING_HOURS));
                            createMessage(id, new String[]{TEXT, "tuesday", FORMAT});
                        }
                        break;
                    case TUESDAY_WORKING_HOURS:
                        LOGGER.info("new service TUESDAY_WORKING_HOURS with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setTuesdayWorkingHours(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.WEDNESDAY_WORKING_HOURS));
                            createMessage(id, new String[]{TEXT, "wednesday", FORMAT});
                        }
                        break;
                    case WEDNESDAY_WORKING_HOURS:
                        LOGGER.info("new service WEDNESDAY_WORKING_HOURS with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setWednesdayWorkingHours(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.THURSDAY_WORKING_HOURS));
                            createMessage(id, new String[]{TEXT, "thursday", FORMAT});
                        }
                        break;
                    case THURSDAY_WORKING_HOURS:
                        LOGGER.info("new service THURSDAY_WORKING_HOURS with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setThursdayWorkingHours(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.FRIDAY_WORKING_HOURS));
                            createMessage(id, new String[]{TEXT, "friday", FORMAT});
                        }
                        break;
                    case FRIDAY_WORKING_HOURS:
                        LOGGER.info("new service FRIDAY_WORKING_HOURS with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setFridayWorkingHours(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.SATURDAY_WORKING_HOURS));
                            createMessage(id, new String[]{TEXT, "saturday", FORMAT});
                        }
                        break;
                    case SATURDAY_WORKING_HOURS:
                        LOGGER.info("new service SATURDAY_WORKING_HOURS with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setSaturdayWorkingHours(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.SUNDAY_WORKING_HOURS));
                            createMessage(id, new String[]{TEXT, "sunday", FORMAT});
                        }
                        break;
                    case SUNDAY_WORKING_HOURS:
                        LOGGER.info("new service SUNDAY_WORKING_HOURS with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setSundayWorkingHours(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.TIME_BETWEEN_CLIENTS));
                            sendBotMessageService.sendMessage(String.valueOf(id), Icon.STOPWATCH.get() + " "
                                    + bundleLanguage.getValue(id, "time_between_clients")
                                    + bundleLanguage.getValue(id, "format_between"));
                        }
                        break;
                    case TIME_BETWEEN_CLIENTS:
                        LOGGER.info("new service TIME_BETWEEN_CLIENTS with message text {}", update.getMessage().getText());
                        if (changeFormatTimeBetweenClients(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setTimeBetweenClients(update.getMessage().getText()));
                            sendBotMessageService.sendMessage(String.valueOf(id), Icon.WHITE_CHECK_MARK.get() + " "
                                    + bundleLanguage.getValue(id, "service_added"));
                            serviceRepository.save(convertToEntity(serviceDTO));
                            ServiceCache.remove(serviceDTO);
                            isRegistration = true;
                        }
                        break;
                    default:
                        //do nothing
                        break;
                }
            }
        }
        return isRegistration;
    }

    /**
     * receiving the telegramId of the user from the message and setting the initial registration status
     *
     * @param id - telegramId user
     * @return - ServiceDto
     */
    private ServiceDTO generateServiceFromMessage(Long id) {
        ua.shpp.eqbot.dto.ServiceDTO dto = new ua.shpp.eqbot.dto.ServiceDTO();
        dto.setTelegramId(id)
                .setPositionRegistrationService(PositionRegistrationService.INPUT_SERVICE_NAME);
        return dto;
    }

    private ServiceDTO addingDescriptionAndAvatar(Message message, ua.shpp.eqbot.dto.ServiceDTO serviceDTO) {
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
        return serviceDTO;
    }

    private boolean changeFormatTime(String time, Long id) {
        if (time.equals("-") || time.matches("(\\d|0\\d|1\\d|2[0-3]):[0-5]\\d-(\\d|0\\d|1\\d|2[0-3]):[0-5]\\d")) {
            return true;
        } else {
            createMessage(id, "unformatted");
            return false;
        }
    }

    private boolean changeFormatTimeBetweenClients(String time, Long id) {
        if (time.matches("(\\d|0\\d|1\\d|2[0-3]):[0-5]\\d")) {
            return true;
        } else {
            createMessage(id, "unformatted");
            return false;
        }
    }

    private ServiceEntity convertToEntity(ua.shpp.eqbot.dto.ServiceDTO dto) {
        if (dto == null) {
            return null;
        }
        ServiceEntity entity = modelMapper.map(dto, ServiceEntity.class);
        LOGGER.info("convert dto to entity");
        return entity;
    }

    private void createMessage(Long id, String text) {
        sendBotMessageService.sendMessage(SendMessage.builder().text(bundleLanguage.getValue(id, text)).chatId(id).build());
    }

    private void createMessage(Long id, String[] text) {
        StringBuilder resultText = new StringBuilder();
        for (String t : text) {
            resultText.append(bundleLanguage.getValue(id, t)).append(" ");
        }
        sendBotMessageService.sendMessage(SendMessage.builder().text(Icon.DATE.get() + " " + resultText.toString()).chatId(id).build());
    }
}
