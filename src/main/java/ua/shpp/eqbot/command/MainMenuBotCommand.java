package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.ArrayList;
import java.util.List;

@Component("mainmenuBotCommand")
public class MainMenuBotCommand implements BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuBotCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final List<org.telegram.telegrambots.meta.api.objects.commands.BotCommand> listOfCommand = new ArrayList<>();
    private final BundleLanguage bundleLanguage;

    @Autowired
    public MainMenuBotCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Override
    public boolean execute(Update update) {
        Long id = update.getMessage().getChatId();
        listOfCommand.add(new org.telegram.telegrambots.meta.api.objects.commands.BotCommand("/start", bundleLanguage.getValue(id, "start")));
        listOfCommand.add(new org.telegram.telegrambots.meta.api.objects.commands.BotCommand("/help", Icon.QUESTION.get() + " " + bundleLanguage.getValue(id, "help")));
        listOfCommand.add(new org.telegram.telegrambots.meta.api.objects.commands.BotCommand("/settings", Icon.SETTINGS.get() + " " + bundleLanguage.getValue(id, "settings")));
        listOfCommand.add(new org.telegram.telegrambots.meta.api.objects.commands.BotCommand("/delete", Icon.NOT.get() + " " + bundleLanguage.getValue(id, "delete")));
        listOfCommand.add(new org.telegram.telegrambots.meta.api.objects.commands.BotCommand("/feedback", Icon.THUMBSUP.get() + " " + bundleLanguage.getValue(id, "feedback")));
        sendBotMessageService.sendMenu(new SetMyCommands(listOfCommand, new BotCommandScopeDefault(), null));
        LOGGER.info("Created main menu successful.");
        return true;
    }
}
