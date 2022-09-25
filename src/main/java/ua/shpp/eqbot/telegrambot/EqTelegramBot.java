package ua.shpp.eqbot.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shpp.eqbot.processors.Processor;
import ua.shpp.eqbot.telegrambot.command.CommandContainer;
import ua.shpp.eqbot.telegrambot.service.SendBotMessageServiceImpl;

import static ua.shpp.eqbot.telegrambot.command.CommandName.NO;

@Component
public class EqTelegramBot extends TelegramLongPollingBot {
    private final static Logger LOGGER = LoggerFactory.getLogger(EqTelegramBot.class);

    public static String COMMAND_PREFIX = "/";
    private final CommandContainer commandContainer;

    public EqTelegramBot() {
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this));
    }


    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    private Processor processor;

    @Override
    public void onUpdateReceived(Update update) {
        processor.process(update);
        LOGGER.info("========================================");
        if (update.hasMessage() && update.getMessage().hasText()) {
            LOGGER.info("Message from {} {} (id = {}).",
                    update.getMessage().getChat().getFirstName(),
                    update.getMessage().getChat().getLastName(),
                    update.getMessage().getChat().getId());

            String messageText = update.getMessage().getText().trim();
            long chatId = update.getMessage().getChatId();
            if (messageText.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = messageText.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            } else if (messageText.equals("Change role to Provider")||messageText.equals("Реестрація нового провайдера")) {
                commandContainer.retrieveCommand(messageText).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }
        }

        /*if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            sendingResponse(chatId, update.getMessage().getText());
        }*/
    }

    private void sendingResponse(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException ex) {
            LOGGER.error("fail execute message {}", message.getText());
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
}
