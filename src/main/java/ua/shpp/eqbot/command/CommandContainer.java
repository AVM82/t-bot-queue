package ua.shpp.eqbot.command;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

/**
 * Container of the {@link Command}s, which are using for handling telegram commands.
 */
@Service
public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    @Autowired
    public CommandContainer(SendBotMessageService sendBotMessageService, UserRepository userRepository) {
        ProviderRepository providerRepository = new ProviderRepository();
        commandMap = ImmutableMap.<String, Command>builder()
                .put(CommandName.REG.getCommandName(), new RegistrationNewUser(sendBotMessageService, userRepository))
                .put(CommandName.START.getCommandName(), new StartCommand(sendBotMessageService))
                .put(CommandName.HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(CommandName.NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(CommandName.SETTINGS.getCommandName(), new SettingsCommand(sendBotMessageService))
                .put(CommandName.CHANGE_ROLE_TO_PROVIDER.getCommandName(), new ChangeRoleToProviderCommand(sendBotMessageService, providerRepository))
                .put(CommandName.REGISTR_NEW_PROVIDER.getCommandName(), new RegistrationNewProviderCommand(sendBotMessageService, providerRepository))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}