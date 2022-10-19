package ua.shpp.eqbot.command.registrationfortheservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.util.ArrayList;
import java.util.List;

public class CreatingButtonField {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatingButtonField.class);
    private final SendBotMessageService sendBotMessageService;
    private final int quantityPerRow;
    private final List<String> listData;
    private final String theMainInscription;
    private final Long id;
    private final String additionalButton;

    public CreatingButtonField(SendBotMessageService sendBotMessageService,
                               int quantityPerRow,
                               List<String> listData,
                               String theMainInscription, Long id, String additionalButton) {
        this.sendBotMessageService = sendBotMessageService;
        this.quantityPerRow = quantityPerRow;
        this.listData = listData;
        this.theMainInscription = theMainInscription;
        this.id = id;
        this.additionalButton = additionalButton;
        addButtons();
    }

    private void addButtons() {
        LOGGER.info("add buttons");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int count = 0;
        for (int row = 0; row <= listData.size() / quantityPerRow; ++row) {
            List<InlineKeyboardButton> button = new ArrayList<>();
            for (int col = 0; col < quantityPerRow; ++col) {
                if (count < listData.size()) {
                    creatingButtonsInRow(button, count);
                } else {
                    continue;
                }
                ++count;
            }
            keyboard.add(button);
        }
        if (!additionalButton.equals("")) {
            listData.add(additionalButton);
            List<InlineKeyboardButton> button = new ArrayList<>();
            creatingButtonsInRow(button, listData.size() - 1);
            keyboard.add(button);
        }
        inlineKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(theMainInscription);
        sendMessage.setChatId(id);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendBotMessageService.sendMessage(sendMessage);
    }

    private void creatingButtonsInRow(List<InlineKeyboardButton> button, int position) {
        button.add(InlineKeyboardButton.builder()
                .text(listData.get(position))
                .callbackData(listData.get(position))
                .build());
    }
}
