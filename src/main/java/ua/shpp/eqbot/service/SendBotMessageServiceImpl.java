package ua.shpp.eqbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shpp.eqbot.dto.PrevPositionDTO;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;

import ua.shpp.eqbot.stage.icon.Icon;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    public static final int PAGE_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger(SendBotMessageServiceImpl.class);

    private final EqTelegramBot telegramBot;

    @Autowired
    public SendBotMessageServiceImpl(EqTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        try {
            telegramBot.execute(sendMessage);
            logger.info("send message {}", message);
        } catch (TelegramApiException e) {
            logger.warn(e.getLocalizedMessage());
        }
    }

    @Override
    public void sendMessage(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            logger.warn(e.getLocalizedMessage());
        }
    }

    @Override
    public void sendMessage(SendPhoto message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            logger.warn(e.getLocalizedMessage());
        }
    }

    @Override
    public void setReplyMarkup(String chatId, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText("Menu");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.warn(e.getLocalizedMessage());
        }
    }

    @Override
    public void sendMenu(SetMyCommands command) {
        try {
            telegramBot.execute(command);
        } catch (TelegramApiException e) {
            logger.warn(e.getLocalizedMessage());
        }
    }


    public List<List<InlineKeyboardButton>> createPageableKeyboard(Page<ServiceEntity> paging, PrevPositionDTO prevPositionDTO, BundleLanguage bundleLanguage) {
        int totPages = paging.getTotalPages();
        List<ServiceEntity> listServices = paging.toList();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        listServices.forEach(service -> {
            List<InlineKeyboardButton> serviceInfo = new ArrayList<>();
            serviceInfo.add(InlineKeyboardButton.builder()
                    .text(service.getName() + " (ID: " + service.getId() + ")")
                    .callbackData("service_info/" + service.getId())
                    .build());
            keyboard.add(serviceInfo);
        });
        List<InlineKeyboardButton> navigationLine = new ArrayList<>();
        int curPage = prevPositionDTO.getPage();
        if (prevPositionDTO.getPage() > 0) {
            navigationLine.add(InlineKeyboardButton.builder()
                    .text(Icon.ARROW_LEFT.get()).callbackData("back").build());
        }
        navigationLine.add(bundleLanguage.createButton(prevPositionDTO.getTelegramId(), "exit", "exit"));
        if (curPage + 1 < totPages) {
            navigationLine.add(InlineKeyboardButton.builder()
                    .text(Icon.ARROW_RIGHT.get()).callbackData("next").build());

        }
        keyboard.add(navigationLine);
        return keyboard;
    }
}
