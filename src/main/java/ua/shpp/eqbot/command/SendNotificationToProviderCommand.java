package ua.shpp.eqbot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.stage.icon.Icon;

import java.util.Collections;
import java.util.List;

public class SendNotificationToProviderCommand implements BotCommand {
    private final ServiceRepository serviceRepository;
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;

    public SendNotificationToProviderCommand(ServiceRepository serviceRepository, SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage) {
        this.serviceRepository = serviceRepository;
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    public void sendNotification(Long serviceId, String timeOfRegistration, String userName, String customerTelegramId) {
        ServiceEntity serviceEntity = serviceRepository.findFirstById(serviceId);
        Long providerChatId = serviceEntity.getTelegramId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(providerChatId);
        String internationalizationAnswer = bundleLanguage.getValue(providerChatId, "send_notification_to_provider");
        String[] answer = internationalizationAnswer.split("\\.");
        sendMessage.setText(Icon.E_MAIL.get() + answer[0] + userName + answer[1] + serviceEntity.getName() + answer[2] + " " + timeOfRegistration);
        sendMessage.setReplyMarkup(InlineKeyboardMarkup.builder().
                keyboard(List.of(Collections.singletonList(bundleLanguage.createButton(providerChatId,
                        "blacklist_add_this_user", "blacklist/add/" + customerTelegramId)))).build());
        String connectMessage = bundleLanguage.getValue(providerChatId, "connect_with_customer");
        sendMessage.setReplyMarkup(InlineKeyboardMarkup.builder().
                keyboard(List.of(List.of(bundleLanguage.createButton(providerChatId,
                                "blacklist_add_this_user", "blacklist/add/" + customerTelegramId)),
                        List.of(InlineKeyboardButton.builder().text(connectMessage).url("tg://user?id=" + customerTelegramId).build()))).build());
        sendBotMessageService.sendMessage(sendMessage);
    }

    @Override
    public boolean execute(Update update) {
        return true;
    }
}