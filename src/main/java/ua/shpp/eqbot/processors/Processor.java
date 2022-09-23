package ua.shpp.eqbot.processors;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Processor {
    void executeMessage(Message message);

    void executeCallbackQuery(CallbackQuery callbackQuery);

    default void process(Update update){
        if(update.hasMessage()){
            executeMessage(update.getMessage());
        }else if(update.hasCallbackQuery()){
            executeCallbackQuery(update.getCallbackQuery());
        }
    }
}
