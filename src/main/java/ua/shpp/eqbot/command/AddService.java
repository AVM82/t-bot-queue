package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.shpp.eqbot.cache.BotUserCache;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;

import static ua.shpp.eqbot.model.PositionMenu.MENU_START;

@Component
public class AddService implements Command {

    private static final Logger log = LoggerFactory.getLogger(AddService.class);
    private final SendBotMessageService sendBotMessageService;
    public static final String ADD_SERVICE_MESSAGE = "input_name_service";
    private ServiceRepository serviceRepository;

    ProvideRepository provideRepository;

    @Autowired
    public AddService(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository, ProvideRepository provideRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.serviceRepository = serviceRepository;
        this.provideRepository = provideRepository;
    }

    public void addService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setId_telegram(service.getId_telegram()).setName(service.getName()).setDescription(service.getDescription());
        serviceRepository.save(serviceEntity);
    }


    @Override
    public boolean execute(Update update) {

        long id ;

        if (update.hasCallbackQuery()){
            id = update.getCallbackQuery().getFrom().getId();
        }else {
            id = update.getMessage().getChatId();
        }


         if (provideRepository.findById_telegram(id)!=null)/*providerRepository.findById(update.getMessage().getChatId())*/
         {
            UserDto user;
            ServiceDTO newService;
             log.info("Switched to provider");
            log.info("Switched to provider");
            if (update.hasMessage()) {
                Long idTelegram = update.getMessage().getChat().getId();
                user = BotUserCache.findBy(idTelegram);
                newService = new ServiceDTO();
                newService.setId_telegram(user.getId_telegram()).setName(update.getMessage().getText().trim());
                user.setPositionMenu(MENU_START);
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(BundleLanguage.getValue(idTelegram, "successfully")).build());
                addService(newService);
            } else {
                Long idTelegram = update.getCallbackQuery().getFrom().getId();
                sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(BundleLanguage.getValue(idTelegram, ADD_SERVICE_MESSAGE)).build());
                log.info("Add new service.");
            }

        } else {
            var markup = new ReplyKeyboardMarkup();
            var keyboardRows = new ArrayList<KeyboardRow>();
            KeyboardRow registrationNewProvider = new KeyboardRow();
            registrationNewProvider.add("Реєстрація нового провайдера");
            keyboardRows.add(registrationNewProvider);
            markup.setKeyboard(keyboardRows);
            markup.setResizeKeyboard(true);
            log.info("Didn't find provider with such id");
            sendBotMessageService.setReplyMarkup(update.getCallbackQuery().getFrom().getId().toString(), markup);
        }


            return false;

    }
}
