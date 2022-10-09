package ua.shpp.eqbot.command;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

/**
 * Container of the {@link ICommand}s, which are using for handling telegram commands.
 */
@Service
public class CommandContainer {

    private final ImmutableMap<String, ICommand> commandMap;
    private final ICommand unknownICommand;

    @Autowired
    public CommandContainer(SendBotMessageService sendBotMessageService, ServiceRepository serviceRepository,
                            ProviderRepository providerRepository, ImageService imageService,
                            BundleLanguage bundleLanguage, UserService userService,
                            ProviderService providerService) {
        commandMap = ImmutableMap.<String, ICommand>builder()
                .put(CommandName.REG.getNameCommand(), new RegistrationNewUserICommand(sendBotMessageService, userService, bundleLanguage))
                .put(CommandName.START.getNameCommand(), new StartICommand(sendBotMessageService, bundleLanguage))
                .put(CommandName.HELP.getNameCommand(), new HelpICommand(sendBotMessageService))
                .put(CommandName.NO.getNameCommand(), new NoCommand(sendBotMessageService))
                .put(CommandName.SETTINGS.getNameCommand(), new SettingsICommand(sendBotMessageService, bundleLanguage))
                .put(CommandName.CHANGE_ROLE_TO_PROVIDER.getNameCommand(),
                        new ChangeRoleToProviderICommand(sendBotMessageService, providerRepository, bundleLanguage))
                .put(CommandName.ADD_SERVICE.getNameCommand(),
                        new RegistrationServiceICommand(sendBotMessageService, serviceRepository, imageService, bundleLanguage))
                .put(CommandName.SEARCH_SERVICE.getNameCommand(),
                        new SearchServiceCommand(sendBotMessageService, serviceRepository, providerRepository, userService, bundleLanguage))
                .put(CommandName.DELETE_USER.getNameCommand(), new DeleteUserICommand(sendBotMessageService,
                        userService, bundleLanguage, providerService, serviceRepository))
                .put(CommandName.MAIN_MENU.getNameCommand(), new MainMenuICommand(sendBotMessageService, bundleLanguage))
                .put(CommandName.CHECK_PROVIDER.getNameCommand(),
                        new CheckProviderRegistrationICommand(sendBotMessageService, providerService, bundleLanguage))
                .put(CommandName.ADD_PROVIDER.getNameCommand(),
                        new RegistrationNewProviderICommand(sendBotMessageService, providerService, bundleLanguage))
                .put(CommandName.CHECK_SERVICE.getNameCommand(),
                        new CheckServiceRegistrationICommand(sendBotMessageService, bundleLanguage, serviceRepository, imageService))
                .put(CommandName.CHANGE_LANGUAGE.getNameCommand(), new ChangeLanguageICommand(sendBotMessageService, userService, bundleLanguage))
                .build();
        unknownICommand = new UnknownICommand(sendBotMessageService, bundleLanguage);
    }

    public ICommand retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownICommand);
    }
}
