package ua.shpp.eqbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.paging.Paging;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
@Service
@Scope("prototype")
public class SendBotMessageServiceImpl implements SendBotMessageService {

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

    @Override
    public SendMessage sendButtonToUser(SendMessage sendMessage, String telegramId, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> line = new ArrayList<>();
        line.add(InlineKeyboardButton.builder().text(text).url("tg://user?id=" + telegramId).build());
        keyboard.add(line);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        ReplyKeyboard replyMarkup = sendMessage.getReplyMarkup();
        if (!(replyMarkup instanceof InlineKeyboardMarkup)) {
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        } else {
            InlineKeyboardMarkup curMarkup = (InlineKeyboardMarkup) replyMarkup;
            List<List<InlineKeyboardButton>> curKeyboard = curMarkup.getKeyboard();
            curKeyboard.add(line);
            curMarkup.setKeyboard(curKeyboard);
            sendMessage.setReplyMarkup(curMarkup);
        }
        return sendMessage;
    }

    public List<List<InlineKeyboardButton>> createPageableKeyboard(Page<ServiceEntity> paging, PrevPositionDTO prevPositionDTO){
        int totPages = paging.getTotalPages();
        List<ServiceEntity> listServices = paging.toList();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        listServices.forEach(service ->{
            List<InlineKeyboardButton> serviceInfo = new ArrayList<>();
            serviceInfo.add(InlineKeyboardButton.builder()
                    .text(service.getName()+ " (ID: " + service.getId() + ")")
                    .callbackData("service_info/"+service.getId())
                    .build());
            keyboard.add(serviceInfo);
        });
        List<InlineKeyboardButton> navigationLine = new ArrayList<>();
        int curPage = prevPositionDTO.getPage();
        if(prevPositionDTO.getPage()>0){
            navigationLine.add(InlineKeyboardButton.builder()
                    .text("<<").callbackData("back").build());
        }
        navigationLine.add(InlineKeyboardButton.builder()
                .text("exit").callbackData("e").build());
        if(curPage+1<totPages){
                navigationLine.add(InlineKeyboardButton.builder()
                        .text(">>").callbackData("next").build());

        }
        keyboard.add(navigationLine);
        return keyboard;
    }
}
