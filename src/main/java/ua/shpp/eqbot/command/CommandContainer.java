package ua.shpp.eqbot.command;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.repository.UserRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

/**
 * Container of the {@link Command}s, which are using for handling telegram commands.
 */
@Service
public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    @Autowired
    public CommandContainer(SendBotMessageService sendBotMessageService, UserRepository userRepository,
                            ServiceRepository serviceRepository, ProvideRepository provideRepository,
                            ImageService imageService, BundleLanguage bundleLanguage, UserService userService) {
        commandMap = ImmutableMap.<String, Command>builder()
                .put(CommandName.REG.getNameCommand(), new RegistrationNewUser(sendBotMessageService, userService, bundleLanguage))
                .put(CommandName.START.getNameCommand(), new StartCommand(sendBotMessageService, bundleLanguage))
                .put(CommandName.HELP.getNameCommand(), new HelpCommand(sendBotMessageService))
                .put(CommandName.NO.getNameCommand(), new NoCommand(sendBotMessageService))
                .put(CommandName.SETTINGS.getNameCommand(), new SettingsCommand(sendBotMessageService))
                .put(CommandName.CHANGE_ROLE_TO_PROVIDER.getNameCommand(),
                        new ChangeRoleToProviderCommand(sendBotMessageService, provideRepository))
                .put(CommandName.ADD_SERVICE.getNameCommand(),
                        new AddService(sendBotMessageService, serviceRepository, imageService, provideRepository, userService, bundleLanguage))
                .put(CommandName.DELETE_USER.getNameCommand(), new DeleteUserCommand(sendBotMessageService,
                        userRepository, userService, bundleLanguage, provideRepository, serviceRepository))
                .put(CommandName.MAIN_MENU.getNameCommand(), new MainMenu(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService, bundleLanguage);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
