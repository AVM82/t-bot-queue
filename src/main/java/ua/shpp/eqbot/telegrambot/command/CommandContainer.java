package ua.shpp.eqbot.telegrambot.command;

import com.google.common.collect.ImmutableMap;
import ua.shpp.eqbot.telegrambot.repository.ProviderRepository;
import ua.shpp.eqbot.telegrambot.service.SendBotMessageService;

import static ua.shpp.eqbot.telegrambot.command.CommandName.*;

/**
 * Container of the {@link Command}s, which are using for handling telegram commands.
 */
public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    private final ProviderRepository providerRepository = new ProviderRepository();

    public CommandContainer(SendBotMessageService sendBotMessageService) {
        commandMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(SETTINGS.getCommandName(), new SettingsCommand(sendBotMessageService))
                .put(CHANGE_ROLE_TO_PROVIDER.getCommandName(), new ChangeRoleToProviderCommand(sendBotMessageService, providerRepository))
                .put(REGISTR_NEW_PROVIDER.getCommandName(), new RegistrationNewProviderCommand(sendBotMessageService, providerRepository))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}