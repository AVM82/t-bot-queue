package ua.shpp.eqbot.command;

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
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.stage.PositionRegistrationService;
import java.util.List;

@Component
public class AddService implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddService.class);
    private final SendBotMessageService sendBotMessageService;
    private final ServiceRepository serviceRepository;
    private final ImageService imageService;
    private final BundleLanguage bundleLanguage;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public AddService(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository,
                      ImageService imageService,BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.imageService = imageService;
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
            if (update.getMessage() != null && !update.getMessage().isCommand()) {
                switch (serviceDTO.getPositionRegistrationService()) {
                    case INPUT_SERVICE_NAME:
                        LOGGER.info("new service INPUT_USERNAME with message text {}", update.getMessage().getText());
                            ServiceCache.add(serviceDTO.setName(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.INPUT_PICTURE));
                            createMessage(id, "add_desc_and_avatar");
                        break;
                    case INPUT_PICTURE:
                        LOGGER.info("new service INPUT_CITY with message text {}", update.getMessage().getText());
                            ServiceCache.add(addingDescriptionAndAvatar(update.getMessage(), serviceDTO)
                                    .setPositionRegistrationService(PositionRegistrationService.START_WORK_IN_MONDAY));
                            createMessage(id, "beginning_of_work", "monday", "format");
                        break;
                    case START_WORK_IN_MONDAY:
                        LOGGER.info("new service START_WORK_IN_MONDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setStartWorkInMonday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.END_WORK_IN_MONDAY));
                            createMessage(id, "end_of_work_on", "monday", "format");
                        }
                        break;
                    case END_WORK_IN_MONDAY:
                        LOGGER.info("new service END_WORK_IN_MONDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setEndWorkInMonday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.START_WORK_IN_TUESDAY));
                            createMessage(id, "beginning_of_work", "tuesday", "format");
                        }
                        break;
                    case START_WORK_IN_TUESDAY:
                        LOGGER.info("new service START_WORK_IN_TUESDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setStartWorkInTuesday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.END_WORK_IN_TUESDAY));
                            createMessage(id, "end_of_work_on", "tuesday", "format");
                        }
                        break;
                    case END_WORK_IN_TUESDAY:
                        LOGGER.info("new service END_WORK_IN_TUESDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setEndWorkInTuesday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.START_WORK_IN_WEDNESDAY));
                            createMessage(id, "beginning_of_work", "wednesday", "format");
                        }
                        break;
                    case START_WORK_IN_WEDNESDAY:
                        LOGGER.info("new service START_WORK_IN_WEDNESDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setStartWorkInWednesday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.END_WORK_IN_WEDNESDAY));
                            createMessage(id, "end_of_work_on", "wednesday", "format");
                        }
                        break;
                    case END_WORK_IN_WEDNESDAY:
                        LOGGER.info("new service END_WORK_IN_WEDNESDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setEndWorkInwWednesday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.START_WORK_IN_THURSDAY));
                            createMessage(id, "beginning_of_work", "thursday", "format");
                        }
                        break;
                    case START_WORK_IN_THURSDAY:
                        LOGGER.info("new service START_WORK_IN_THURSDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setStartWorkInThursday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.END_WORK_IN_THURSDAY));
                            createMessage(id, "end_of_work_on", "thursday", "format");
                        }
                        break;
                    case END_WORK_IN_THURSDAY:
                        LOGGER.info("new service END_WORK_IN_THURSDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setEndWorkInThursday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.START_WORK_IN_FRIDAY));
                            createMessage(id, "beginning_of_work", "friday", "format");
                        }
                        break;
                    case START_WORK_IN_FRIDAY:
                        LOGGER.info("new service START_WORK_IN_FRIDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setStartWorkInFriday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.END_WORK_IN_FRIDAY));
                            createMessage(id, "end_of_work_on", "friday", "format");
                        }
                        break;
                    case END_WORK_IN_FRIDAY:
                        LOGGER.info("new service END_WORK_IN_FRIDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setEndWorkInFriday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.START_WORK_IN_SATURDAY));
                            createMessage(id, "beginning_of_work", "saturday", "format");
                        }
                        break;
                    case START_WORK_IN_SATURDAY:
                        LOGGER.info("new service START_WORK_IN_SATURDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setStartWorkInSaturday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.END_WORK_IN_SATURDAY));
                            createMessage(id, "end_of_work_on", "saturday", "format");
                        }
                        break;
                    case END_WORK_IN_SATURDAY:
                        LOGGER.info("new service END_WORK_IN_SATURDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setEndWorkInSaturday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.START_WORK_IN_SUNDAY));
                            createMessage(id, "beginning_of_work", "sunday", "format");
                        }
                        break;
                    case START_WORK_IN_SUNDAY:
                        LOGGER.info("new service START_WORK_IN_SUNDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setStartWorkInSunday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.END_WORK_IN_SUNDAY));
                            createMessage(id, "end_of_work_on", "sunday", "format");
                        }
                        break;
                    case END_WORK_IN_SUNDAY:
                        LOGGER.info("new service END_WORK_IN_SUNDAY with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setEndWorkInSunday(update.getMessage().getText())
                                    .setPositionRegistrationService(PositionRegistrationService.TIME_BETWEEN_CLIENTS));
                            createMessage(id, "time_between_clients", "format");
                        }
                        break;
                    case TIME_BETWEEN_CLIENTS:
                        LOGGER.info("new service TIME_BETWEEN_CLIENTS with message text {}", update.getMessage().getText());
                        if (changeFormatTime(update.getMessage().getText(), id)) {
                            ServiceCache.add(serviceDTO.setTimeBetweenClients(update.getMessage().getText()));
                            createMessage(id, "service_added");
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

    private ServiceDTO generateServiceFromMessage(Long id) {
        ServiceDTO dto = new ServiceDTO();
        dto.setIdTelegram(id)
                .setPositionRegistrationService(PositionRegistrationService.INPUT_SERVICE_NAME);
        return dto;
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
        return serviceDTO;
    }

    private boolean changeFormatTime(String time, Long id){
        if (time.matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]"))
            return true;
        else {
            createMessage(id,"unformatted");
            return false;
        }
    }

    private ServiceEntity convertToEntity(ServiceDTO dto) {
        if (dto == null) return null;
        ServiceEntity entity = modelMapper.map(dto, ServiceEntity.class);
        LOGGER.info("convert dto to entity");
        return entity;
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
