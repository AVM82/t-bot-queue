package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.*;

@Component
public class BlacklistBotCommand implements BotCommand {
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
        if (update.hasMessage()) {
            messageHandler(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            callbackHandler(update.getCallbackQuery());
        }
        return true;
    }

    private void messageHandler(Message message) {
        long telegramId = message.getChatId();
        UserDto user = userService.getDto(telegramId);
        String input = message.getText();
        SendMessage sendMessage = new SendMessage();
        if (user.getPositionMenu().equals(PositionMenu.BLACKLIST_ADD)) {
            sendMessage = addUserToBlackList(telegramId, input);
        } else if (user.getPositionMenu().equals(PositionMenu.BLACKLIST_DELETE)) {
            sendMessage = deleteUserFromBlacklist(telegramId, input);
        }
        sendMessage.setChatId(telegramId);
        sendBotMessageService.sendMessage(sendMessage);

    }

    private void callbackHandler(CallbackQuery callbackQuery) {
        long telegramId = callbackQuery.getFrom().getId();
        UserDto user = userService.getDto(telegramId);
        String task = callbackQuery.getData().split("/")[1];
        SendMessage sendMessage;
        switch (task) {
            case "main":
                sendMessage = blackListMain(telegramId);
            break;
            case "add":
                sendMessage = blackListAdd(telegramId, callbackQuery.getData());
                if (user.getPositionMenu() != PositionMenu.BLACKLIST_ADD) {
                    user.setPositionMenu(PositionMenu.BLACKLIST_ADD);
                }
            break;
            case "delete":
                sendMessage = blackListDelete(telegramId, callbackQuery.getData());
                if (user.getPositionMenu() != PositionMenu.BLACKLIST_DELETE) {
                    user.setPositionMenu(PositionMenu.BLACKLIST_DELETE);
                }
            break;
            default:
                sendMessage = new SendMessage();
                break;
        }
        sendMessage.setChatId(telegramId);
        sendBotMessageService.sendMessage(sendMessage);
    }

    private SendMessage blackListMain(long telegramId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setChatId(telegramId);
        ProviderEntity provider = providerService.getByTelegramIdEntity(telegramId);
        String message;
        HashSet<Long> blackList = provider.getBlacklist();
        if (blackList.isEmpty()) {
            message = Icon.NOTEBOOK.get() + " "  + bundleLanguage.getValue(telegramId, "no_blacklist");
        } else {
            HashSet<Long> blacklist = provider.getBlacklist();
            StringBuilder sb = new StringBuilder(Icon.NOTEBOOK.get() + " " + bundleLanguage.getValue(telegramId, "users_in_blacklist")).append("\n");
            blacklist.forEach(userId -> {
                sb.append("<a href=\"tg://user?id=").append(userId).append("\">");
                UserEntity user = userService.getEntity(userId);
                if (user != null) {
                    sb.append(user.getName());
                }
                sb.append("[").append(userId).append("]</a>\n");
            });
            message = sb.toString();
        }
        sendMessage.setText(message);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> addButton = new ArrayList<>();
        addButton.add(bundleLanguage.createButton(telegramId, "add_user_to_blacklist", "blacklist/add"));
        keyboard.add(addButton);
        List<InlineKeyboardButton> deleteButton = new ArrayList<>();
        deleteButton.add(bundleLanguage.createButton(telegramId, "delete_from_blacklist", "blacklist/delete"));
        keyboard.add(deleteButton);
        List<InlineKeyboardButton> backButton = new ArrayList<>();
        backButton.add(bundleLanguage.createButton(telegramId, "exit", "exit"));
        keyboard.add(backButton);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    SendMessage blackListAdd(long telegramId, String callbackData) {
        if (callbackData.split("/").length <= 2) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(bundleLanguage.getValue(telegramId, "blacklist_enter_add_user"));
            return sendMessage;
        } else {
            String userIdString = callbackData.split("/")[2];
            return addUserToBlackList(telegramId, userIdString);
        }
    }

    SendMessage blackListDelete(long telegramId, String callbackData) {
        if (callbackData.split("/").length <= 2) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(bundleLanguage.getValue(telegramId, "blacklist_enter_delete_user"));
            return sendMessage;
        } else {
            String userIdString = callbackData.split("/")[2];
            return deleteUserFromBlacklist(telegramId, userIdString);
        }
    }

    SendMessage addUserToBlackList(long telegramId, String userIdString) {
        SendMessage sendMessage = new SendMessage();
        if (!userIdString.matches("\\d*")) {
            sendMessage.setText(bundleLanguage.getValue(telegramId, "blacklist_invalid_input"));
        } else {
            long userId = Long.parseLong(userIdString);
            ProviderEntity provider = providerService.getByTelegramIdEntity(telegramId);
            HashSet<Long> blackList = provider.getBlacklist();
            if (blackList.contains(userId)) {
                sendMessage.setText(bundleLanguage.getValue(telegramId, "blacklist_user_already_in_list"));
            } else {
                blackList.add(userId);
                provider.setBlacklist(blackList);
                providerService.saveEntity(provider);
                sendMessage.setText(bundleLanguage.getValue(telegramId, "blacklist_user_added_to_list"));
            }
            sendMessage.setReplyMarkup(InlineKeyboardMarkup.builder()
                    .keyboardRow(Collections.singletonList(InlineKeyboardButton.builder()
                            .text(bundleLanguage.getValue(telegramId, "my_blacklist"))
                            .callbackData("blacklist/main").build())).build());
        }
        return sendMessage;
    }

    SendMessage deleteUserFromBlacklist(long telegramId, String userIdString) {
        SendMessage sendMessage = new SendMessage();
        if (!userIdString.matches("\\d*")) {
            sendMessage.setText(bundleLanguage.getValue(telegramId, "blacklist_invalid_input"));
        } else {
            long userId = Long.parseLong(userIdString);
            ProviderEntity provider = providerService.getByTelegramIdEntity(telegramId);
            HashSet<Long> blackList = provider.getBlacklist();
            if (!blackList.contains(userId)) {
                sendMessage.setText(bundleLanguage.getValue(telegramId, "blacklist_delete_no_id"));
            } else {
                blackList.remove(userId);
                provider.setBlacklist(blackList);
                providerService.saveEntity(provider);
                sendMessage.setText(bundleLanguage.getValue(telegramId, "blacklist_user_deleted_from_list"));
            }
            sendMessage.setReplyMarkup(InlineKeyboardMarkup.builder()
                    .keyboardRow(Collections.singletonList(InlineKeyboardButton.builder()
                            .text(bundleLanguage.getValue(telegramId, "my_blacklist"))
                            .callbackData("blacklist/main").build())).build());
        }
        return sendMessage;
    }

}
