package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.BotUserCache;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.service.SendBotMessageService;

import static ua.shpp.eqbot.model.PositionMenu.MENU_START;

public class AddService implements Command {

    private static final Logger log = LoggerFactory.getLogger(AddService.class);
    private final SendBotMessageService sendBotMessageService;
    public static final String ADD_SERVICE_MESSAGE = "Введіть назву послуги:\n";

    public AddService(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
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

        } else {
            Long idTelegram = update.getCallbackQuery().getFrom().getId();
            sendBotMessageService.sendMessage(SendMessage.builder().chatId(idTelegram).text(ADD_SERVICE_MESSAGE).build());
            log.info("Add new service.");
        }

        return false;
    }
}
