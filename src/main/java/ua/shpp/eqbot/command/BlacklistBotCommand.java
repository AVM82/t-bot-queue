package ua.shpp.eqbot.command;

import org.apache.catalina.startup.SetNextNamingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class BlacklistBotCommand implements BotCommand{
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;
    private final UserService userService;
    private final ProviderService providerService;

    @Autowired
    public BlacklistBotCommand(SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage, UserService userService, ProviderService providerService) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
        this.userService = userService;
        this.providerService = providerService;
    }



    @Override
    public boolean execute(Update update) {
        if(update.hasMessage()){
            messageHandler(update.getMessage());
        }else if (update.hasCallbackQuery()){
            callbackHandler(update.getCallbackQuery());
        }
        return true;
    }

    private void messageHandler(Message message){

    }

    private void callbackHandler(CallbackQuery callbackQuery){
        long telegramId = callbackQuery.getFrom().getId();
        String task = callbackQuery.getData().split("/")[1];
        SendMessage sendMessage;
        switch (task){
            case "main":{
                sendMessage = blackListMain(telegramId);
            }

        }
    }

    SendMessage blackListMain(long telegramId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setChatId(telegramId);
        ProviderEntity provider = providerService.getByTelegramIdEntity(telegramId);
        String message;
        if(provider.getBlackList()==null || provider.getBlackList().isEmpty()){
            message = bundleLanguage.getValue(telegramId, "no_blacklist");
        }else {
            HashSet<Long> blacklist = provider.getBlackList();
            StringBuilder sb = new StringBuilder(bundleLanguage.getValue(telegramId, "user_in_blacklist:<br>"));
            blacklist.forEach(userId -> {
                sb.append("<a href=\"tg://user?id=").append(userId).append("\"><br>");
            });
            sb.delete(sb.length()-4,sb.length());
            message = sb.toString();
        }
    }

}
