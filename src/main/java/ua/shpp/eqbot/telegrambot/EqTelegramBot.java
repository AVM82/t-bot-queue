package ua.shpp.eqbot.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shpp.eqbot.command.CommandContainer;
import ua.shpp.eqbot.command.CommandName;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageServiceImpl;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;

import static ua.shpp.eqbot.stage.PositionMenu.*;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);
    private final CommandContainer commandContainer;
    private final UserService userService;
    ProviderRepository providerRepository;

    @Autowired
    public EqTelegramBot(ServiceRepository serviceRepository,
                         ProviderRepository providerRepository, @Lazy ImageService imageService,
                         BundleLanguage bundleLanguage, UserService userService,
                         ProviderService providerService) {
        this.providerRepository = providerRepository;
        this.userService = userService;
        this.commandContainer = new CommandContainer(
                new SendBotMessageServiceImpl(this),
                serviceRepository,
                providerRepository,
                imageService,
                bundleLanguage,
                userService,
                providerService);
    }

    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
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
            if (!commandContainer.retrieveCommand("/reg").execute(update)) {
                LOGGER.info("Registration user");
                //change registration provider
            } else {
                UserDto user = userService.getDto(update.getMessage().getChat().getId());
                if (user.getPositionMenu() == REGISTRATION_PROVIDER) {
                    if (commandContainer.retrieveCommand("/add provider").execute(update)
                            && !commandContainer.retrieveCommand("/check service").execute(update)) {
                        user.setPositionMenu(PositionMenu.REGISTRATION_SERVICE);
                    }
                    //change registration service
                } else if (user.getPositionMenu() == REGISTRATION_SERVICE) {
                    if (commandContainer.retrieveCommand("/add").execute(update)) {
                        user.setPositionMenu(MENU_START);
                        commandContainer.retrieveCommand("/start").execute(update);
                    }
                } else if (user.getPositionMenu() == PositionMenu.MENU_START) {
                    commandContainer.retrieveCommand("mainMenu").execute(update);
                    commandContainer.retrieveCommand("/start").execute(update);
                }
            }
        }

    }


    private void commandHandler(Update update) {
        String messageText = update.getMessage().getText().trim();
        String commandIdentifier = messageText.split(" ")[0].toLowerCase();
        LOGGER.info("new command here {}", commandIdentifier);

        if (commandIdentifier.equals("/start")) {
            if (commandContainer.retrieveCommand("/reg").execute(update)) {
                commandContainer.retrieveCommand("/start").execute(update);
            }
        } else {
            commandContainer.retrieveCommand(commandIdentifier).execute(update);
        }
    }

    private void callbackQueryHandler(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        UserDto userDto = userService.getDto(update.getCallbackQuery().getFrom().getId());
        if (callbackQuery.getData().equals("create_service")) {
            LOGGER.info("create_service");
            userDto.setPositionMenu(MENU_CREATE_SERVICE);
            if (!commandContainer.retrieveCommand("/check provider").execute(update)) {
                userDto.setPositionMenu(REGISTRATION_PROVIDER);
            }
        } else if (callbackQuery.getData().equals("search_service")) {
            LOGGER.info("search_menu");
            commandContainer.retrieveCommand(CommandName.SEARCH_MENU.getNameCommand()).execute(update);
        } else if (callbackQuery.getData().equals("searchName")) {
            LOGGER.info("search by name");
            userDto.setPositionMenu(SEARCH_BY_NAME);
            if (!commandContainer.retrieveCommand("/search").execute(update)) {
                userDto.setPositionMenu(MENU_START);
                commandContainer.retrieveCommand("/start").execute(update);
            }
        } else if (callbackQuery.getData().equals("searchId")) {
            LOGGER.info("search by id");

        } else if (callbackQuery.getData().equals("return_in_menu")) {
            commandContainer.retrieveCommand("/start").execute(update);
        } else if (callbackQuery.getData().equals("change_provider_details")) {
            LOGGER.info("change provider details");
        } else if (callbackQuery.getData().equals("newServiceFromAnExistingProvider")) {
            LOGGER.info("add new service");
            userService.getDto(update.getCallbackQuery().getFrom().getId())
                    .setPositionMenu(PositionMenu.REGISTRATION_SERVICE);
            commandContainer.retrieveCommand("/add").execute(update);
        } else if (userDto.getPositionMenu() == SEARCH_BY_NAME) {
            LOGGER.info("The user has successfully selected the service");
            try {
                execute(SendMessage.builder()
                        .chatId(callbackQuery.getFrom().getId())
                        .text(callbackQuery.getData())
                        .build());
            } catch (TelegramApiException e) {
                LOGGER.warn(e.getLocalizedMessage());
            }
            userDto.setPositionMenu(DONE);
        } else if (callbackQuery.getData().equals("add_provider")) {
            commandContainer.retrieveCommand("/add provider").execute(update);
            UserDto user = userService.getDto(update.getCallbackQuery().getFrom().getId());
            user.setPositionMenu(REGISTRATION_PROVIDER);
        } else if (callbackQuery.getData().equals("change_role")) {
            commandContainer.retrieveCommand("/change_role").execute(update);
        } else if (callbackQuery.getData().equals("change_lang")) {
            commandContainer.retrieveCommand("/change_language").execute(update);
            commandContainer.retrieveCommand("/start").execute(update);
        }
    }
}
