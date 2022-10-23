package ua.shpp.eqbot.command.registeringyouruser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.command.BotCommand;
import ua.shpp.eqbot.command.RegistrationNewUserBotCommand;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.mapper.UserMapper;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.PositionRegistration;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.Date;

@Component("recordyouruserBotCommand")
public class RecordYourUserBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordYourUserBotCommand.class);
    private final BundleLanguage bundleLanguage;
    private final UserService userService;
    private final ServiceRepository serviceRepository;
    private final SendBotMessageService sendBotMessageService;

    public RecordYourUserBotCommand(BundleLanguage bundleLanguage,
                                    UserService userService,
                                    ServiceRepository serviceRepository,
                                    SendBotMessageService sendBotMessageService) {
        this.bundleLanguage = bundleLanguage;
        this.userService = userService;
        this.serviceRepository = serviceRepository;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {
        boolean isRegistration = false;
        Long telegramId;
        if (update.hasCallbackQuery()) {
            telegramId = update.getCallbackQuery().getFrom().getId();
        } else if (update.hasMessage()) {
            telegramId = update.getMessage().getChatId();
        } else {
            return false;
        }
        UserDto userDto = userService.getDto(telegramId);
        try {
            switch (userDto.getPositionMenu()) {
                case MENU_CREATE_SERVICE:
                    LOGGER.info("start registration customer, userDto = {}", userDto);
                    //для реестрації юзера на сервіс силами проавайдера потрібно створити юзера в базі данних,
                    //так як його телеграм невідомий то за telegramID приймаємо відбемне число(кількість секунд Unix)
                    Long telegramIdCustomer = System.currentTimeMillis() / -1000L;
                    UserDto customerDto = new UserDto();
                    userDto.setPositionMenu(PositionMenu.ADD_USERNAME_CUSTOMER)
                            .setCustomerDto(customerDto.setTelegramId(telegramIdCustomer)
                                    .setLanguage(userDto.getLanguage())
                                    .setPositionMenu(PositionMenu.DONE)
                                    .setPositionRegistration(PositionRegistration.DONE));
                    sendBotMessageService.sendMessage(createMessage(telegramId, bundleLanguage.getValue(telegramId,
                            "name_customer")));
                    break;
                case ADD_USERNAME_CUSTOMER:
                    LOGGER.info("registration customer, userDto = {}", userDto);
                    userDto.setPositionMenu(PositionMenu.ADD_PHONE_CUSTOMER)
                            .setCustomerDto(userDto.getCustomerDto()
                                    .setName(update.getMessage().getText()));
                    sendBotMessageService.sendMessage(createMessage(telegramId, bundleLanguage.getValue(telegramId,
                            "phone_customer")));
                    break;
                case ADD_PHONE_CUSTOMER:
                    LOGGER.info("end registration customer, userDto = {}", userDto);
                    userDto.setPositionMenu(PositionMenu.REGISTRATION_FOR_THE_SERVICES_START)
                            .setCustomerDto(userDto.getCustomerDto()
                                    .setPhone(update.getMessage().getText()));
                    userService.saveEntity(UserMapper.INSTANCE.userDTOToUserEntity(userDto.getCustomerDto()));
                    isRegistration = true;
                    break;
                default:
                    //do nothing
                    break;
            }
        } catch (Exception exception) {
            sendBotMessageService.sendMessage(createMessage(telegramId, bundleLanguage.getValue(telegramId,
                    "error_registration")));
            userDto.setCustomerDto(null).setPositionMenu(PositionMenu.MENU_START);
        }
        return isRegistration;
    }

    private SendMessage createMessage(Long chatId, String text) {
        return SendMessage.builder()
                .text(text)
                .chatId(String.valueOf(chatId))
                .build();
    }
}
