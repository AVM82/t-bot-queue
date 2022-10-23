package ua.shpp.eqbot.service;

import org.springframework.data.domain.Page;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.PrevPositionDTO;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;

import java.util.List;

/**
 * Service for sending messages via telegram-bot.
 */
public interface SendBotMessageService {


    /**
     * Send message via telegram bot.
     *
     * @param chatId  provided chatId in which messages would be sent.
     * @param message provided message to be sent.
     */
    void sendMessage(String chatId, String message);

    void sendMessage(SendMessage message);

    void sendMessage(SendPhoto message);

    void setReplyMarkup(String chatId, ReplyKeyboardMarkup replyKeyboardMarkup);

    void sendMenu(SetMyCommands command);

    List<List<InlineKeyboardButton>> createPageableKeyboard(Page<ServiceEntity> paging, PrevPositionDTO prevPositionDTO, BundleLanguage bundleLanguage);
}
