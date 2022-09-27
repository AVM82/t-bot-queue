package ua.shpp.eqbot.command;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.repository.ServiceRepository;
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
    public CommandContainer(SendBotMessageService sendBotMessageService, UserRepository userRepository, ServiceRepository serviceRepository) {
        ProviderRepository providerRepository = new ProviderRepository();
        commandMap = ImmutableMap.<String, Command>builder()
                .put(CommandName.REG.getCommand(), new RegistrationNewUser(sendBotMessageService, userRepository))
                .put(CommandName.START.getCommand(), new StartCommand(sendBotMessageService))
                .put(CommandName.HELP.getCommand(), new HelpCommand(sendBotMessageService))
                .put(CommandName.NO.getCommand(), new NoCommand(sendBotMessageService))
                .put(CommandName.SETTINGS.getCommand(), new SettingsCommand(sendBotMessageService))
                .put(CommandName.CHANGE_ROLE_TO_PROVIDER.getCommand(), new ChangeRoleToProviderCommand(sendBotMessageService, providerRepository))
                .put(CommandName.REGISTER_NEW_PROVIDER.getCommand(), new RegistrationNewProviderCommand(sendBotMessageService, providerRepository))
                .put(CommandName.ADD_SERVICE.getCommand(), new AddService(sendBotMessageService, serviceRepository))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}