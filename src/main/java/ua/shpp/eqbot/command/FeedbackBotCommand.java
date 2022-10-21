package ua.shpp.eqbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.FeedbackEntity;
import ua.shpp.eqbot.repository.FeedbackRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.ArrayList;
import java.util.List;

@Component("feedbackBotCommand")
public class FeedbackBotCommand implements BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackBotCommand.class);
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;
    private final UserService userService;
    private final FeedbackRepository feedbackRepository;

    public FeedbackBotCommand(SendBotMessageService sendBotMessageService,
                              BundleLanguage bundleLanguage,
                              UserService userService,
                              FeedbackRepository feedbackRepository) {
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
        this.userService = userService;
        this.feedbackRepository = feedbackRepository;
    }


    @Override
    public boolean execute(Update update) {
        long id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        } else {
            id = update.getMessage().getChatId();
        }

        UserDto user = userService.getDto(id);
        if (user.getPositionMenu() != PositionMenu.FEEDBACK) {
            user.setPositionMenu(PositionMenu.FEEDBACK);
            LOGGER.info("Generate feedback menu");
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> availableServiceButtons = new ArrayList<>();
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(InlineKeyboardButton.builder()
                    .text(Icon.CRY.get())
                    .callbackData("1")
                    .build());
            button.add(InlineKeyboardButton.builder()
                    .text(Icon.FLUSHED.get())
                    .callbackData("2")
                    .build());
            button.add(InlineKeyboardButton.builder()
                    .text(Icon.HEART_EYES.get())
                    .callbackData("3")
                    .build());
            availableServiceButtons.add(button);
            inlineKeyboardMarkup.setKeyboard(availableServiceButtons);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(bundleLanguage.getValue(id, "feedback.text"));
            sendMessage.setChatId(id);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendBotMessageService.sendMessage(sendMessage);
        } else {
            user.setPositionMenu(PositionMenu.MENU_START);
            if (update.hasCallbackQuery()) {
                LOGGER.info("Save rate");
                feedbackRepository.save(new FeedbackEntity().setTelegramId(id)
                        .setRate(Integer.parseInt(update.getCallbackQuery().getData())));
            } else {
                LOGGER.info("Save text feedback");
                feedbackRepository.save(new FeedbackEntity().setTelegramId(id).setTextFeedback(update.getMessage().getText()));
            }
            sendBotMessageService.sendMessage(String.valueOf(id), bundleLanguage.getValue(id, "feedback.answer")
                    + Icon.HEARTPULSE.get());
        }
        return true;
    }
}
