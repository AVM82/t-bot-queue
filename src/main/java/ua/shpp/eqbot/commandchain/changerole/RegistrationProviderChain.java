package ua.shpp.eqbot.commandchain.changerole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.shpp.eqbot.command.*;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;

public class RegistrationProviderChain implements CommandChain {

    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationProviderChain.class);
    private final ArrayList<Command> ChangeRoleChain = new ArrayList<>();
    private int i = (-1);



    public RegistrationProviderChain (SendBotMessageService sendBotMessageService, ProvideRepository provideRepository) {
        ChangeRoleChain.add(new RegistrationNewProviderCommand(sendBotMessageService));
        ChangeRoleChain.add((new AddProviderNameCommand(sendBotMessageService, provideRepository)));
        ChangeRoleChain.add(new AddCityToProviderCommand(sendBotMessageService,provideRepository));
    }

    @Override
    public Command nextCommand(){
        i++;
        LOGGER.info("Call next command in ChangeRoleChain");
        return ChangeRoleChain.get(i);

    }

    @Override
    public boolean hasNextCommand () {
        if (i > 1){
            LOGGER.info("ChangeRoleChain ended");
            return false;
        }
        LOGGER.info("ChangeRoleChain has next command");
        return true;
    }
}
