package ua.shpp.eqbot.commandchain.changerole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.shpp.eqbot.command.*;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.HashMap;

public class RegistrationProviderChain implements CommandChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationProviderChain.class);

    private final HashMap<RegistrationProviderStep, Command> changeRoleChain = new HashMap<>();

    private RegistrationProviderStep thisStep = RegistrationProviderStep.REG_MESSAGE_STEP;


    public RegistrationProviderChain(SendBotMessageService sendBotMessageService, ProvideRepository provideRepository) {
        changeRoleChain.put(RegistrationProviderStep.REG_MESSAGE_STEP, new RegistrationNewProviderCommand(sendBotMessageService));
        changeRoleChain.put(RegistrationProviderStep.REG_ADD_NAME_STEP, new AddProviderNameCommand(sendBotMessageService, provideRepository));
        changeRoleChain.put(RegistrationProviderStep.REG_ADD_CITY_STEP, new AddCityToProviderCommand(sendBotMessageService, provideRepository));
        changeRoleChain.put(RegistrationProviderStep.REG_DONE, new NoCommand(sendBotMessageService));
    }

    @Override
    public Command nextCommand() {
        Command stepCommand = changeRoleChain.get(thisStep);
        thisStep = thisStep.next(thisStep);
        LOGGER.info("Call next command in ChangeRoleChain");
        return stepCommand;
    }

    @Override
    public boolean hasNextCommand() {
        if (thisStep == RegistrationProviderStep.REG_DONE) {
            LOGGER.info("ChangeRoleChain ended");
            return false;
        }
        LOGGER.info("ChangeRoleChain has next command");
        return true;
    }
}
