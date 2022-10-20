package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Container of the {@link BotCommand}s, which are using for handling telegram commands.
 */
@Service
public class CommandContainer {

    private final Map<String, BotCommand> commandMap;

    @Autowired
    public CommandContainer(Map<String, BotCommand> iCommands) {
        commandMap = iCommands;
    }

    public BotCommand retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, null);
    }
}
