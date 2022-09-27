package ua.shpp.eqbot.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.BotUserCache;
import ua.shpp.eqbot.command.CommandContainer;
import ua.shpp.eqbot.commandchain.changerole.CommandChain;
import ua.shpp.eqbot.commandchain.changerole.RegistrationProviderChain;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.model.PositionMenu;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageServiceImpl;

import static ua.shpp.eqbot.command.CommandName.NO;
import static ua.shpp.eqbot.model.PositionMenu.MENU_CREATE_SERVICE;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    private final static Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);
    private final CommandContainer commandContainer;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private boolean isCommandChain = false;

    private CommandChain commandChain;
    ProvideRepository provideRepository;


    @Autowired
    public EqTelegramBot(UserRepository userRepository, ServiceRepository serviceRepository, ProvideRepository provideRepository) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.provideRepository = provideRepository;
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), userRepository, serviceRepository, provideRepository);
    }

    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            callbackQueryHandler(update);
        }else if(update.getMessage().getText().equals("Change role to Provider")){
            commandContainer.retrieveCommand(update.getMessage().getText()).execute(update);
        } else if (update.getMessage().hasText() && update.getMessage().isCommand()) {
            commandHandler(update);
        } else if (update.getMessage().getText().equals("Реєстрація нового провайдера")) {
            createRegistrationProviderCommandChain(update);
        } else if(isCommandChain){
            callNextCommandInChain(update);
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
            UserDto user = BotUserCache.findBy(update.getMessage().getChat().getId());
            if(!commandContainer.retrieveCommand("/reg").execute(update)){
                LOGGER.info("Registration user");
            }else if (user.getPositionMenu() == MENU_CREATE_SERVICE){
                commandContainer.retrieveCommand("/add").execute(update);
                commandContainer.retrieveCommand("/start").execute(update);
            } else if(user.getPositionMenu() == PositionMenu.MENU_START) {
                    commandContainer.retrieveCommand("/start").execute(update);
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
            if (messageText.equals("Change role to Provider") || messageText.equals("Реєстрація нового провайдера")) {
                commandContainer.retrieveCommand(messageText).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }
        }
    }

    private void callbackQueryHandler(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery.getData().equals("create_service")) {
            LOGGER.info("create_service");
            UserDto user = BotUserCache.findBy(update.getCallbackQuery().getFrom().getId());
            user.setPositionMenu(MENU_CREATE_SERVICE);
            commandContainer.retrieveCommand("/add").execute(update);
        }
        if (callbackQuery.getData().equals("search_service")) {
            LOGGER.info("search_service");
        }
    }

    public void createRegistrationProviderCommandChain (Update update) {
        commandChain = new RegistrationProviderChain(new SendBotMessageServiceImpl(this), provideRepository);
        isCommandChain = true;
        commandChain.nextCommand().execute(update);
    }

    public void callNextCommandInChain(Update update) {
        if (commandChain.hasNextCommand()){
            commandChain.nextCommand().execute(update);
        } else {
            isCommandChain = false;
        }
    }

}
