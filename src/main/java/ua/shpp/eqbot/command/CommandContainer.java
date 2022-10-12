package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import ua.shpp.eqbot.command.registrationfortheservice.RegistrationForTheServiceCommand;
import ua.shpp.eqbot.command.registrationprovider.CheckProviderRegistrationICommand;
import ua.shpp.eqbot.command.registrationprovider.RegistrationNewProviderICommand;
import ua.shpp.eqbot.command.registrationservice.CheckServiceRegistrationICommand;
import ua.shpp.eqbot.command.registrationservice.RegistrationServiceICommand;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.RegistrationForTheServiceRepository;
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

    private final Map<String, ICommand> commandMap;

    @Autowired
    public CommandContainer(Map<String, ICommand> iCommands) {
        commandMap = iCommands;
    }

    public ICommand retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, null);
    }
}
