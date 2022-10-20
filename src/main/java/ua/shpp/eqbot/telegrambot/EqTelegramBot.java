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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shpp.eqbot.command.CommandContainer;
import ua.shpp.eqbot.command.CommandName;
import ua.shpp.eqbot.command.ICommand;
import ua.shpp.eqbot.command.registrationfortheservice.RegistrationForTheServiceCommand;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.mapper.UserMapper;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.RegistrationForTheServiceRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageServiceImpl;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.PositionRegistration;

import java.util.Map;

import static ua.shpp.eqbot.stage.PositionMenu.*;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);
    private final UserService userService;
    private final Map<String, ICommand> iCommands;

    @Autowired
    public EqTelegramBot(@Lazy Map<String, ICommand> iCommands, UserService userService) {
        this.iCommands = iCommands;
        this.userService = userService;
    }


    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    /*TODO command return answer*/
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            callbackQueryHandler(update);
        } else if (update.getMessage().hasText() && update.getMessage().isCommand()) {
            commandHandler(update);
        } else {
            textHandler(update);
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
            //change registration user
            if (!iCommands.get("/reg").execute(update)) {
                LOGGER.info("Registration user");
                //change registration provider
            } else {
                UserDto user = userService.getDto(update.getMessage().getChat().getId());
                if (user.getPositionMenu() == REGISTRATION_PROVIDER) {
                    if (iCommands.get("/add provider").execute(update)
                            && !iCommands.get("/check service").execute(update)) {
                        user.setPositionMenu(PositionMenu.REGISTRATION_SERVICE);
                    }
                    //change registration service
                } else if (user.getPositionMenu() == REGISTRATION_SERVICE) {
                    if (iCommands.get("/add").execute(update)) {
                        user.setPositionMenu(MENU_START);
                        iCommands.get(CommandName.START.getNameCommand()).execute(update);
                    }
                } else if (user.getPositionMenu() == PositionMenu.MENU_START) {
                    iCommands.get("mainMenu").execute(update);
                    iCommands.get(CommandName.START.getNameCommand()).execute(update);
                } else if (user.getPositionMenu() == SEARCH_BY_ID) {
                    LOGGER.info("input id for search");
                    if (!iCommands.get(CommandName.SEARCH_BY_ID.getNameCommand()).execute(update)) {
                        iCommands.get(CommandName.SEARCH_MENU.getNameCommand()).execute(update);
                    }
                } else if (user.getPositionMenu().equals(SEARCH_BY_CITY_NAME)) {
                    LOGGER.info("Entering a city to search for a service");
                    if (!iCommands.get(CommandName.SEARCH_SERVICE.getNameCommand()).execute(update)) {
                        user.setPositionMenu(MENU_START);
                        iCommands.get(CommandName.START.getNameCommand()).execute(update);
                    }
                } else if (user.getPositionMenu() == SEARCH_USES_NAME_SERVICE) {
                    LOGGER.info("enter a few letters that you want to search for");
                    iCommands.get(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update);
//                    if (!iCommands.get(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update)) {
//                        iCommands.get(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update);
//                    }

                }
            }
        }

    }


    private void commandHandler(Update update) {
        String messageText = update.getMessage().getText().trim();
        String commandIdentifier = messageText.split(" ")[0].toLowerCase();
        LOGGER.info("new command here {}", commandIdentifier);

        if (commandIdentifier.equals(CommandName.START.getNameCommand())) {
            if (iCommands.get("/reg").execute(update)) {
                iCommands.get(CommandName.START.getNameCommand()).execute(update);
            }
        } else {
            iCommands.get(commandIdentifier).execute(update);
        }
    }

    private void callbackQueryHandler(Update update) {
        LOGGER.info("callbackQueryHandler start work");
        CallbackQuery callbackQuery = update.getCallbackQuery();
        UserDto userDto = findDtoIfPossible(update);
        if (update.getCallbackQuery().getData().startsWith("appoint/")) {
            userDto.setPositionMenu(REGISTRATION_FOR_THE_SERVICES_START);
        }
        if (callbackQuery.getData().equals("create_service")) {
            LOGGER.info("create_service");
            userDto.setPositionMenu(MENU_CREATE_SERVICE);
            if (!iCommands.get("/check provider").execute(update)) {
                userDto.setPositionMenu(REGISTRATION_PROVIDER);
            }
        } else if (callbackQuery.getData().equals("search_service")) {
            LOGGER.info("search_menu");
            iCommands.get(CommandName.SEARCH_MENU.getNameCommand()).execute(update);
        } else if (callbackQuery.getData().equals("searchName")) {
            LOGGER.info("search by name");
            iCommands.get(CommandName.SEARCH_SERVICE.getNameCommand()).execute(update);
        } else if (callbackQuery.getData().equals("searchId")) {
            LOGGER.info("search by id");
            iCommands.get(CommandName.SEARCH_BY_ID.getNameCommand()).execute(update);
        } else if (callbackQuery.getData().equals("searchString")) {
            LOGGER.info("search uses name service");
            iCommands.get(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update);
        } else if (callbackQuery.getData().equals("return_in_menu")) {
            iCommands.get(CommandName.START.getNameCommand()).execute(update);
        } else if (callbackQuery.getData().equals("change_provider_details")) {
            LOGGER.info("change provider details");
        } else if (callbackQuery.getData().equals("newServiceFromAnExistingProvider")) {
            LOGGER.info("add new service");
            userService.getDto(update.getCallbackQuery().getFrom().getId())
                    .setPositionMenu(PositionMenu.REGISTRATION_SERVICE);
            iCommands.get("/add").execute(update);
        } else if (userDto.getPositionMenu() == SEARCH_BY_NAME
                || userDto.getPositionMenu() == REGISTRATION_FOR_THE_SERVICES_DATE
                || userDto.getPositionMenu() == REGISTRATION_FOR_THE_SERVICES_TIME) {
            if (callbackQuery.getData().matches("\\d+:?.?\\d*") || callbackQuery.getData().equals("змінити дату")) {
                LOGGER.info("The user has successfully selected the service");
                RegistrationForTheServiceCommand.setNumberOfDaysInSearchOfService(7);
                if (iCommands.get("/RegistrationForTheServiceCommand").execute(update)) {
                    iCommands.get(CommandName.START.getNameCommand()).execute(update);
                }
            } else {
                if (!iCommands.get(CommandName.SEARCH_SERVICE.getNameCommand()).execute(update)) {
                    userDto.setPositionMenu(MENU_START);
                    iCommands.get(CommandName.START.getNameCommand()).execute(update);
                }
            }
        } else if (callbackQuery.getData().equals("add_provider")) {
            iCommands.get("/add provider").execute(update);
            UserDto user = userService.getDto(update.getCallbackQuery().getFrom().getId());
            user.setPositionMenu(REGISTRATION_PROVIDER);
        } else if (callbackQuery.getData().equals("change_role")) {
            iCommands.get("/change_role").execute(update);
        } else if (callbackQuery.getData().equals("next") || callbackQuery.getData().equals("back")) {
            iCommands.get(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update);
        } else if (callbackQuery.getData().equals("exit")) {
            iCommands.get(CommandName.SEARCH_USES_NAME_SERVICE.getNameCommand()).execute(update);
            iCommands.get(CommandName.START.getNameCommand()).execute(update);
        } else if (callbackQuery.getData().equals("change_lang")) {
            iCommands.get("/change_language").execute(update);
            iCommands.get("/start").execute(update);
        } else {
            iCommands.get("/start").execute(update);
        }
    }

    private UserDto findDtoIfPossible(Update update) {
        long telegramId = update.getCallbackQuery().getFrom().getId();
        UserDto userDto = userService.getDto(telegramId);
        UserEntity entity = null;
        if (userDto == null) {
            entity = userService.getEntity(telegramId);
        }
        if (entity != null) {
            userDto = UserMapper.INSTANCE.userEntityToUserDTO(entity);
            userDto.setPositionMenu(MENU_START);
            userDto.setPositionRegistration(PositionRegistration.DONE);
            userService.saveDto(userDto);
        }
        return userDto;
    }


}
