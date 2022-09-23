package ua.shpp.eqbot.processors;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.shpp.eqbot.hadlers.CallbackQueryHandler;
import ua.shpp.eqbot.hadlers.MessageHandler;

@Component
public class DefaultProcessor implements Processor{

    private final CallbackQueryHandler callbackQueryHandler;
    private final MessageHandler messageHandler;

    public DefaultProcessor(CallbackQueryHandler callbackQueryHandler, MessageHandler messageHandler) {
        this.callbackQueryHandler = callbackQueryHandler;
        this.messageHandler = messageHandler;
    }

    @Override
    public void executeMessage(Message message) {
        messageHandler.choose(message);
    }

    @Override
    public void executeCallbackQuery(CallbackQuery callbackQuery) {
        callbackQueryHandler.choose(callbackQuery);
    }
}
