package ua.shpp.eqbot.hadlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery>{
    @Override
    public void choose(CallbackQuery callbackQuery) {

    }
}
