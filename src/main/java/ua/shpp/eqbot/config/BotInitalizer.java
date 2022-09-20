package ua.shpp.eqbot.config;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ua.shpp.eqbot.service.TelegramBot;

@Component
public class BotInitalizer {
    private static final Logger log = LoggerFactory.getLogger(BotInitalizer.class);
    @Autowired
    TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException{
        TelegramBotsApi telegramBotApi = new TelegramBotsApi(DefaultBotSession.class);
        try{
            telegramBotApi.registerBot(bot);
        }catch (TelegramApiException ex){
            log.error("initialization error");
        }
    }
}
