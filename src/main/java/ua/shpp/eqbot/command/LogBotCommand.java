package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.dto.BotCommandResultDto;

@Component
public class LogBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogBotCommand.class);

    @Override
    public BotCommandResultDto execute(Update update) {
        LOGGER.info("========= LOGGER CLASS ===============");
        return new BotCommandResultDto().setDone(true);
    }
}
