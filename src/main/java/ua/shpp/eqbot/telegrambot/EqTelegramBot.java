package ua.shpp.eqbot.telegrambot;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.command.CommandContainer;
import ua.shpp.eqbot.model.UserDto;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.SendBotMessageServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static ua.shpp.eqbot.command.CommandName.NO;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    private final static Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);
    boolean isNewUser = true;
    private final Map<Long, UserDto> users_active_session = new HashMap<>();
    private final CommandContainer commandContainer;
    /*TODO repository here ?*/
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public EqTelegramBot(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), userRepository);
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

    private UserDto convertToDto(UserEntity userEntity) {
        UserDto postDto = modelMapper.map(userEntity, UserDto.class);
        LOGGER.info("convert entity to dto");
        return postDto;
    }

    private void textHandler(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            LOGGER.info("Message from {} {} (id = {}).",
                    update.getMessage().getChat().getFirstName(),
                    update.getMessage().getChat().getLastName(),
                    update.getMessage().getChat().getId());
            if(!isRegistered(update)){
                /*TODO  add logic here*/
            } else {


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

    }

    private boolean isRegistered(Update update) {
        long telegram_id = update.getMessage().getChatId();
        if (!users_active_session.containsKey(telegram_id)) {
            LOGGER.info("user absent into cash user");
            UserEntity userEntity = userRepository.findFirstById_telegram(telegram_id);
            if (userEntity != null) {
                users_active_session.put(telegram_id, convertToDto(userEntity));
                isNewUser = false;
                LOGGER.info("user present into repo");
            } else {
                isNewUser = true;
                LOGGER.info("new user go to registration method");
                boolean execute = commandContainer.retrieveCommand("/reg").execute(update);
                if (execute) {
                    isNewUser = false;
                    userEntity = userRepository.findFirstById_telegram(telegram_id);
                    users_active_session.put(telegram_id, convertToDto(userEntity));
                }
            }
        }
        return false;
    }
}
