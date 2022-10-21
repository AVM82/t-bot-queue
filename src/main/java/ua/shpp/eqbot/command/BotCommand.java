package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.dto.BotCommandResultDto;

/**
 * Command interface for handling telegram-bot commands.
 */
public interface BotCommand {
    /**
     * Main method, which is executing command logic.
     *
     * @param update provided {@link Update} object with all the needed data for command.
     */
    BotCommandResultDto execute(Update update);
}
