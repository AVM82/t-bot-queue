package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

public class MainMenuICommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuICommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final List<BotCommand> listOfCommand = new ArrayList<>();
    private final BundleLanguage bundleLanguage;

    public MainMenuICommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        Long id = update.getMessage().getChatId();
        listOfCommand.add(new BotCommand("/start", bundleLanguage.getValue(id, "start")));
        listOfCommand.add(new BotCommand("/help", bundleLanguage.getValue(id, "help")));
        listOfCommand.add(new BotCommand("/settings", bundleLanguage.getValue(id, "settings")));
        listOfCommand.add(new BotCommand("/delete", bundleLanguage.getValue(id, "delete")));
        sendBotMessageService.sendMenu(new SetMyCommands(listOfCommand, new BotCommandScopeDefault(), null));
        LOGGER.info("Created main menu successful.");
        return true;
    }
}
