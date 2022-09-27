package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.BotUserCache;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import static ua.shpp.eqbot.model.PositionMenu.MENU_START;
@Component
public class AddService implements Command {

    private static final Logger log = LoggerFactory.getLogger(AddService.class);
    private final SendBotMessageService sendBotMessageService;
    public static final String ADD_SERVICE_MESSAGE = "Введіть назву послуги:\n";
    private ServiceRepository serviceRepository;

    @Autowired
    public AddService(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
    }

    public void addService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setId_telegram(service.getId_telegram()).setName(service.getName()).setDescription(service.getDescription());
        serviceRepository.save(serviceEntity);
    }


    @Override
    public boolean execute(Update update) {
        UserDto user;
        ServiceDTO newService;
        if (update.hasMessage()) {
            Long idTelegram = update.getMessage().getChat().getId();
            user = BotUserCache.findBy(idTelegram);
            newService = new ServiceDTO();
            newService.setId_telegram(user.getId_telegram()).setName(update.getMessage().getText().trim());
            user.setPositionMenu(MENU_START);
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text("Успішно!").build());
            addService(newService);
        } else {
            Long idTelegram = update.getCallbackQuery().getFrom().getId();
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(ADD_SERVICE_MESSAGE).build());
            log.info("Add new service.");
        }

        return false;
    }
}
