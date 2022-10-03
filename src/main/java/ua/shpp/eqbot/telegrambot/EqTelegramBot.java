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
import ua.shpp.eqbot.commandchain.changerole.CommandChain;
import ua.shpp.eqbot.commandchain.changerole.RegistrationProviderChain;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.PositionMenu;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageServiceImpl;
import ua.shpp.eqbot.service.UserService;

import static ua.shpp.eqbot.command.CommandName.NO;
import static ua.shpp.eqbot.model.PositionMenu.MENU_CREATE_SERVICE;
import static ua.shpp.eqbot.model.PositionMenu.MENU_SEARCH_SERVICE;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);
    private final CommandContainer commandContainer;
    private final UserService userService;
    private boolean isCommandChain = false;
    private CommandChain commandChain;
    private final BundleLanguage bundleLanguage;
    ProviderRepository providerRepository;


    @Autowired
    public EqTelegramBot(UserRepository userRepository, ServiceRepository serviceRepository, ProviderRepository providerRepository, @Lazy ImageService imageService, BundleLanguage bundleLanguage, ServiceRepository serviceRepository1, UserService userService) {
        this.providerRepository = providerRepository;
        this.userService = userService;
        this.bundleLanguage = bundleLanguage;
        this.commandContainer = new CommandContainer(
                new SendBotMessageServiceImpl(this),
                userRepository,
                serviceRepository,
                providerRepository,
                imageService,
                bundleLanguage,
                userService);
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
        } else if (isCommandChain) {
            callNextCommandInChain(update);
        } else if (update.getMessage().hasPhoto()) {
            imageHandler(update);
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            LOGGER.info("Message from {} {} (id = {}).",
                    update.getMessage().getChat().getFirstName(),
                    update.getMessage().getChat().getLastName(),
                    update.getMessage().getChat().getId());
            UserDto user = userService.getDto(update.getMessage().getChat().getId());
            if (!commandContainer.retrieveCommand("/reg").execute(update)) {
                LOGGER.info("Registration user");
            } else if (update.getMessage().getText().equals("Change role to Provider")) {
                commandContainer.retrieveCommand(update.getMessage().getText()).execute(update);
            } else if (update.getMessage().getText().equals("Реєстрація нового провайдера")) {
                createRegistrationProviderCommandChain(update, bundleLanguage);
            } else if (user.getPositionMenu() == MENU_CREATE_SERVICE) {
                commandContainer.retrieveCommand("/add").execute(update);
            } else if (user.getPositionMenu() == MENU_SEARCH_SERVICE) {
                commandContainer.retrieveCommand("/search").execute(update);
            } else if (user.getPositionMenu() == PositionMenu.MENU_START) {
                commandContainer.retrieveCommand("mainMenu").execute(update);
                commandContainer.retrieveCommand("/start").execute(update);
            }
        }
    }

    private void imageHandler(Update update) {
        UserDto user = userService.getDto(update.getMessage().getChat().getId());
        if (user.getPositionMenu() == MENU_CREATE_SERVICE) {
            commandContainer.retrieveCommand("/add").execute(update);
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
            if (messageText.equals("Change role to Provider") || messageText.equals("Реєстрація нового провайдера")) {
                commandContainer.retrieveCommand(messageText).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getNameCommand()).execute(update);
            }
        }
    }

    private void callbackQueryHandler(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery.getData().equals("create_service")) {
            LOGGER.info("create_service");
            UserDto user = userService.getDto(update.getCallbackQuery().getFrom().getId());
            user.setPositionMenu(MENU_CREATE_SERVICE);
            commandContainer.retrieveCommand("/add").execute(update);
        } else if (callbackQuery.getData().equals("search_service")) {
            LOGGER.info("search_service");
            UserDto user = userService.getDto(update.getCallbackQuery().getFrom().getId());
            user.setPositionMenu(MENU_SEARCH_SERVICE);
            commandContainer.retrieveCommand("/search").execute(update);
        } else {
            LOGGER.info("List of services");
            try {
                execute(SendMessage.builder()
                        .chatId(callbackQuery.getFrom().getId())
                        .text(callbackQuery.getData())
                        .build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createRegistrationProviderCommandChain(Update update, BundleLanguage bundleLanguage) {
        commandChain = new RegistrationProviderChain(new SendBotMessageServiceImpl(this), providerRepository, bundleLanguage);
        isCommandChain = true;
        commandChain.nextCommand().execute(update);
    }

    public void callNextCommandInChain(Update update) {
        if (commandChain.hasNextCommand()) {
            commandChain.nextCommand().execute(update);
            isCommandChain = commandChain.hasNextCommand();
        } else {
            isCommandChain = false;
        }
    }
}
