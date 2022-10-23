package ua.shpp.eqbot.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.command.BotCommand;
import ua.shpp.eqbot.command.CommandName;
import ua.shpp.eqbot.command.UnknownBotCommand;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.mapper.UserMapper;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.PositionRegistration;
import ua.shpp.eqbot.util.CommandUtils;

import java.util.Map;

import static ua.shpp.eqbot.command.registrationfortheservice.RegistrationServiceBotCommand.setNumberOfDaysInSearchOfService;
import static ua.shpp.eqbot.stage.PositionMenu.*;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);
    private final UserService userService;
    private final Map<String, BotCommand> iCommands;

    @Autowired
    public EqTelegramBot(@Lazy Map<String, BotCommand> iCommands, UserService userService) {
        this.iCommands = iCommands;
        this.userService = userService;
    }


    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                callbackQueryHandler(update);
            } else if (update.getMessage().hasText() && update.getMessage().isCommand()) {
                commandHandler(update);
            } else {
                textHandler(update);
            }
        } catch (Exception ex) {
            LOGGER.warn(ex.getLocalizedMessage());
            getBotCommand(CommandName.START.getNameCommand()).execute(update);
        }
    }


    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void textHandler(Update update) {
        if (update.hasMessage() && (update.getMessage().hasText() || update.getMessage().hasPhoto())) {
            LOGGER.info("Message from {} {} (id = {}).",
                    update.getMessage().getChat().getFirstName(),
                    update.getMessage().getChat().getLastName(),
                    update.getMessage().getChat().getId());
            if (!getBotCommand("/reg").execute(update)) {
                LOGGER.info("Registration user");
                //change registration provider
            } else {
                UserDto user = userService.getDto(update.getMessage().getChat().getId());
                if (user.getPositionMenu() == REGISTRATION_PROVIDER) {
                    if (getBotCommand("/add provider").execute(update)
                            && !getBotCommand("/check service").execute(update)) {
                        user.setPositionMenu(PositionMenu.REGISTRATION_SERVICE);
                    }
                    //change registration service
                } else if (user.getPositionMenu() == REGISTRATION_SERVICE) {
                    if (getBotCommand("/add").execute(update)) {
                        user.setPositionMenu(MENU_START);
                        getBotCommand(CommandName.START.getNameCommand()).execute(update);
                    }
                } else if (user.getPositionMenu() == PositionMenu.MENU_START) {
                    getBotCommand("/mainMenu").execute(update);
                    getBotCommand(CommandName.START.getNameCommand()).execute(update);
                } else if (user.getPositionMenu() == SEARCH_BY_ID) {
                    LOGGER.info("input id for search");
                    if (!getBotCommand(CommandName.SEARCH_BY_ID.getNameCommand()).execute(update)) {
                        getBotCommand(CommandName.SEARCH_MENU.getNameCommand()).execute(update);
                    }
                } else if (user.getPositionMenu().equals(SEARCH_BY_CITY_NAME)) {
                    LOGGER.info("Entering a city to search for a service");
                    if (!getBotCommand(CommandName.SEARCH_SERVICE_BY_CITY_NAME.getNameCommand()).execute(update)) {
                        user.setPositionMenu(MENU_START);
                        getBotCommand(CommandName.START.getNameCommand()).execute(update);
                    }
                } else if (user.getPositionMenu() == SEARCH_USES_NAME_SERVICE) {
                    LOGGER.info("enter a few letters that you want to search for");
                    getBotCommand(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update);
                } else if (user.getPositionMenu() == FEEDBACK) {
                    getBotCommand(CommandName.FEEDBACK.getNameCommand()).execute(update);
                    getBotCommand(CommandName.START.getNameCommand()).execute(update);
                } else if (user.getPositionMenu() == BLACKLIST_ADD || user.getPositionMenu() == BLACKLIST_DELETE) {
                    getBotCommand("/blacklist").execute(update);
                } else if (user.getPositionMenu() == ADD_PHONE_CUSTOMER
                        || user.getPositionMenu() == ADD_USERNAME_CUSTOMER) {
                    if (getBotCommand(CommandName.RECORD_YOUR_USER.getNameCommand()).execute(update)) {
                        setNumberOfDaysInSearchOfService(7);
                        getBotCommand("/RegistrationForTheServiceCommand").execute(update);
                    }
                }
            }
        }
    }


    private void commandHandler(Update update) {
        String messageText = update.getMessage().getText().trim();
        String commandIdentifier = messageText.split(" ")[0].toLowerCase();
        LOGGER.info("new command here {}", commandIdentifier);

        if (commandIdentifier.equals(CommandName.START.getNameCommand())) {
            if (getBotCommand("/reg").execute(update)) {
                getBotCommand(CommandName.START.getNameCommand()).execute(update);
            }
        } else {
            getBotCommand(commandIdentifier).execute(update);
        }
    }

    private BotCommand getBotCommand(String botCommand) {
        return iCommands.getOrDefault(CommandUtils.buildFirstLowerClassName(botCommand),
                iCommands.get(CommandUtils.buildFirstLowerClassName(UnknownBotCommand.class.getSimpleName())));
    }

    private void callbackQueryHandler(Update update) {
        LOGGER.info("callbackQueryHandler start work");
        CallbackQuery callbackQuery = update.getCallbackQuery();
        UserDto userDto = findDtoIfPossible(update);
        if (userDto == null) {
            getBotCommand(CommandName.START.getNameCommand()).execute(update);
        } else {
            if (update.getCallbackQuery().getData().startsWith("appoint/")) {
                userDto.setPositionMenu(REGISTRATION_FOR_THE_SERVICES_START);
            }
            if (callbackQuery.getData().equals("create_service")) {
                LOGGER.info("create_service");
                userDto.setPositionMenu(MENU_CREATE_SERVICE);
                if (!getBotCommand("/check provider").execute(update)) {
                    userDto.setPositionMenu(REGISTRATION_PROVIDER);
                }
            } else if (callbackQuery.getData().equals("search_service")) {
                LOGGER.info("search_menu");
                getBotCommand(CommandName.SEARCH_MENU.getNameCommand()).execute(update);
            } else if (callbackQuery.getData().startsWith("searchCity")) {
                LOGGER.info("search by name");
                getBotCommand(CommandName.SEARCH_SERVICE_BY_CITY_NAME.getNameCommand()).execute(update);
            } else if (callbackQuery.getData().startsWith("searchId")) {
                LOGGER.info("search by id");
                getBotCommand(CommandName.SEARCH_BY_ID.getNameCommand()).execute(update);
            } else if (callbackQuery.getData().startsWith("searchString")) {
                LOGGER.info("search uses name service");
                getBotCommand(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update);
            } else if (callbackQuery.getData().equals("return_in_menu")) {
                getBotCommand(CommandName.START.getNameCommand()).execute(update);
            } else if (callbackQuery.getData().equals("change_provider_details")) {
                LOGGER.info("change provider details");
            } else if (callbackQuery.getData().equals("register_the_client")) {
                userDto.setPositionMenu(MENU_CREATE_SERVICE);
                getBotCommand(CommandName.RECORD_YOUR_USER.getNameCommand()).execute(update);
            } else if (callbackQuery.getData().equals("newServiceFromAnExistingProvider")) {
                LOGGER.info("add new service");
                userService.getDto(update.getCallbackQuery().getFrom().getId())
                        .setPositionMenu(PositionMenu.REGISTRATION_SERVICE);
                getBotCommand("/add").execute(update);
            } else if (callbackQuery.getData().startsWith("service_info/")) {
                getBotCommand("/service info").execute(update);
            } else if (callbackQuery.getData().equals("exit")) {
                getBotCommand("/start").execute(update);
            } else if ((userDto.getPositionMenu() == SEARCH_BY_CITY_NAME)) {
                if (!getBotCommand(CommandName.SEARCH_SERVICE_BY_CITY_NAME.getNameCommand()).execute(update)) {
                    userDto.setPositionMenu(MENU_START);
                    getBotCommand(CommandName.START.getNameCommand()).execute(update);
                }
            } else if (userDto.getPositionMenu() == REGISTRATION_FOR_THE_SERVICES_START
                    || userDto.getPositionMenu() == REGISTRATION_FOR_THE_SERVICES_DATE
                    || userDto.getPositionMenu() == REGISTRATION_FOR_THE_SERVICES_TIME) {
                LOGGER.info("The user has successfully selected the service");
                setNumberOfDaysInSearchOfService(7);
                if (getBotCommand("/RegistrationForTheServiceCommand").execute(update)) {
                    getBotCommand(CommandName.START.getNameCommand()).execute(update);
                }
            } else if (callbackQuery.getData().equals("add_provider")) {
                getBotCommand("/add provider").execute(update);
                UserDto user = userService.getDto(update.getCallbackQuery().getFrom().getId());
                user.setPositionMenu(REGISTRATION_PROVIDER);
            } else if (callbackQuery.getData().equals("change_role")) {
                getBotCommand("/change_role").execute(update);
            } else if (callbackQuery.getData().equals("next") || callbackQuery.getData().equals("back")) {
                getBotCommand(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update);
            } else if (callbackQuery.getData().equals("exit")) {
                getBotCommand(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update);
                getBotCommand(CommandName.START.getNameCommand()).execute(update);
            } else if (callbackQuery.getData().equals("change_lang")) {
                getBotCommand("/change_language").execute(update);
                getBotCommand("/start").execute(update);
            } else if (userDto.getPositionMenu() == FEEDBACK) {
                getBotCommand(CommandName.FEEDBACK.getNameCommand()).execute(update);
                getBotCommand(CommandName.START.getNameCommand()).execute(update);
            } else if (callbackQuery.getData().startsWith("blacklist/")) {
                getBotCommand("/blacklist").execute(update);
            } else {
                getBotCommand("/start").execute(update);
            }
        }
    }

    private UserDto findDtoIfPossible(Update update) {
        long telegramId = update.getCallbackQuery().getFrom().getId();
        UserDto userDto = userService.getDto(telegramId);
        UserEntity entity;
        if (userDto == null) {
            entity = userService.getEntity(telegramId);
            if (entity != null) {
                userDto = UserMapper.INSTANCE.userEntityToUserDTO(entity);
                userDto.setPositionMenu(MENU_START);
                userDto.setPositionRegistration(PositionRegistration.DONE);
                userService.saveDto(userDto);
            }
        }
        return userDto;
    }
}
