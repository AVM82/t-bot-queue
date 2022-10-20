package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class LogBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogBotCommand.class);

    @Override
    public boolean execute(Update update) {
        LOGGER.info("========= LOGGER CLASS ===============");
        return true;
    }
}
