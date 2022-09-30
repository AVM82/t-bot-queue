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

public class MainMenu implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenu.class);

    private final SendBotMessageService sendBotMessageService;
    List<BotCommand> listOfCommand = new ArrayList<>();

    public MainMenu(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public boolean execute(Update update) {
//        Long id = update.getMessage().getChatId();
        listOfCommand.add(new BotCommand("/start", "Старт")); //bundleLanguage.getValue(id, "menu_start")
        listOfCommand.add(new BotCommand("/help", "Допомога"));
        listOfCommand.add(new BotCommand("/reg", "Реєстрація"));
        listOfCommand.add(new BotCommand("/stop", "Стоп"));
        listOfCommand.add(new BotCommand("/settings", "Налаштування"));
        listOfCommand.add(new BotCommand("/add", "Додати"));
        listOfCommand.add(new BotCommand("/delete", "Видалити"));
        sendBotMessageService.sendMenu(new SetMyCommands(listOfCommand, new BotCommandScopeDefault(), null));
        LOGGER.info("Created main menu successful.");
        return true;
    }
}
