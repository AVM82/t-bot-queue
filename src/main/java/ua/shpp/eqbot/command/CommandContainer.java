package ua.shpp.eqbot.command;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.repository.SaveService;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;

/**
 * Container of the {@link Command}s, which are using for handling telegram commands.
 */
@Service
public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    @Autowired
    public CommandContainer(SendBotMessageService sendBotMessageService, UserRepository userRepository,
                            ServiceRepository serviceRepository, ProvideRepository provideRepository) {
        commandMap = ImmutableMap.<String, Command>builder()
                .put(CommandName.REG.getCommandName(), new RegistrationNewUser(sendBotMessageService,
                        userRepository))
                .put(CommandName.START.getCommandName(), new StartCommand(sendBotMessageService))
                .put(CommandName.HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(CommandName.NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(CommandName.SETTINGS.getCommandName(), new SettingsCommand(sendBotMessageService))
                .put(CommandName.CHANGE_ROLE_TO_PROVIDER.getCommandName(),
                        new ChangeRoleToProviderCommand(sendBotMessageService, provideRepository))
                .put(CommandName.ADD_SERVICE.getCommandName(), new AddService(sendBotMessageService, serviceRepository,imageService,provideRepository))
                .put(CommandName.DELETE_USER.getCommandName(), new DeleteUserCommand(sendBotMessageService,
                        userRepository, provideRepository, serviceRepository))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}