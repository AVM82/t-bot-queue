package ua.shpp.eqbot.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.command.CommandContainer;
import ua.shpp.eqbot.commandchain.changerole.CommandChain;
import ua.shpp.eqbot.commandchain.changerole.RegistrationProviderChain;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageServiceImpl;

import static ua.shpp.eqbot.command.CommandName.NO;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    private final static Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);
    private final CommandContainer commandContainer;
    private final UserRepository userRepository;

    private boolean isCommandChain = false;

    private CommandChain commandChain;
    ProvideRepository provideRepository;

    @Autowired
    public EqTelegramBot(UserRepository userRepository, ProvideRepository provideRepository) {
        this.userRepository = userRepository;
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), userRepository, provideRepository);
        this.provideRepository = provideRepository;
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
            if(!commandContainer.retrieveCommand("/reg").execute(update)){
                LOGGER.info("Registration user");
            }else{

            }
        }
    }

    private void commandHandler(Update update) {
        String messageText = update.getMessage().getText().trim();
        String commandIdentifier = messageText.split(" ")[0].toLowerCase();
        LOGGER.info("new command here {}", commandIdentifier);
        commandContainer.retrieveCommand(commandIdentifier).execute(update);
        if (messageText.equals("Change role to Provider") || messageText.equals("Реєстрація нового провайдера")) {
            commandContainer.retrieveCommand(messageText).execute(update);
        } else {
            commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
        }
    }

    private void callbackQueryHandler(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery.getData().equals("create_service")) {
            LOGGER.info("create_service");
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
