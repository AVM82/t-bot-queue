package ua.shpp.eqbot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.shpp.eqbot.dto.PrevPositionDTO;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ImageService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceInfoBotCommand implements BotCommand {
    private final ServiceRepository serviceRepository;
    private final SendBotMessageService sendBotMessageService;
    private final BundleLanguage bundleLanguage;
    private final ImageService imageService;
    private final UserService userService;

    @Autowired
    public ServiceInfoBotCommand(ServiceRepository serviceRepository, SendBotMessageService sendBotMessageService, BundleLanguage bundleLanguage, ImageService imageService, UserService userService) {
        this.serviceRepository = serviceRepository;
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
        this.imageService = imageService;
        this.userService = userService;
    }

    private void sendServiceInfoToCreator(ServiceEntity service) {
        Long telegramId = service.getTelegramId();
        String serviceInfo = messageInfo(service, telegramId);
        SendPhoto sendPhoto = createMessage(service, telegramId, serviceInfo);
        if (sendPhoto == null) {
            SendMessage sendMessage = new SendMessage(telegramId.toString(), serviceInfo);
            sendMessage.setReplyMarkup(keyboardForServiceCreator(telegramId));
            sendMessage.setParseMode(ParseMode.HTML);
            sendBotMessageService.sendMessage(sendMessage);
        } else {
            sendPhoto.setReplyMarkup(keyboardForServiceCreator(telegramId));
            sendPhoto.setParseMode(ParseMode.HTML);
            sendBotMessageService.sendMessage(sendPhoto);
        }
    }

    private void sendServiceInfo(ServiceEntity service, Long chatId) {
        String serviceInfo = messageInfo(service, chatId);
        SendPhoto sendPhoto = createMessage(service, chatId, serviceInfo);
        if (sendPhoto == null) {
            SendMessage sendMessage = new SendMessage(chatId.toString(), serviceInfo);
            sendMessage.setReplyMarkup(keyboardForService(service, chatId));
            sendMessage.setParseMode(ParseMode.HTML);
            sendBotMessageService.sendMessage(sendMessage);
        } else {
            sendPhoto.setReplyMarkup(keyboardForService(service, chatId));
            sendPhoto.setParseMode(ParseMode.HTML);
            sendBotMessageService.sendMessage(sendPhoto);
        }
    }

    private InlineKeyboardMarkup keyboardForServiceCreator(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> line1 = new ArrayList<>();
        String messageChange = bundleLanguage.getValue(chatId, "change_service");
        line1.add(InlineKeyboardButton.builder().text(messageChange).callbackData("1").build());
        keyboard.add(line1);
        List<InlineKeyboardButton> line2 = new ArrayList<>();
        String messageRecord = bundleLanguage.getValue(chatId, "add_record_to_service");
        line2.add(InlineKeyboardButton.builder().text(messageRecord).callbackData("register_the_client").build());
        keyboard.add(line2);
        keyboard.add(backButton(chatId));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup keyboardForService(ServiceEntity service, Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> line1 = new ArrayList<>();
        String messageAppointment = bundleLanguage.getValue(chatId, "make_an_appointment");
        line1.add(InlineKeyboardButton.builder().text(messageAppointment).callbackData("appoint/" + service.getId()).build());
        keyboard.add(line1);
        keyboard.add(backButton(chatId));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> backButton(Long chatId) {
        List<InlineKeyboardButton> backButtonLine = new ArrayList<>();
        String messageBack = bundleLanguage.getValue(chatId, "return_back");
        String callbackData = getCallbackData(chatId);
        backButtonLine.add(InlineKeyboardButton.builder().text(messageBack).callbackData(callbackData).build());
        return backButtonLine;
    }

    private String getCallbackData(Long chatId) {
        PrevPositionDTO prevPositionDTO = userService.getPrevPosition(chatId);
        UserDto userDto = userService.getDto(chatId);
        userDto.setPositionMenu(prevPositionDTO.getPositionMenu());
        return prevPositionDTO.getReceivedData();
    }


    private String messageInfo(ServiceEntity service, Long chatId) {
        StringBuilder sb = new StringBuilder("<b>").append(service.getName()).append("</b>");
        sb.append("\n");
        sb.append(service.getDescription());
        sb.append("\n");
        String providerMessage = bundleLanguage.getValue(chatId, "service_provider");
        sb.append("<a href=\"tg://user?id=").append(service.getTelegramId()).append("\">");
        sb.append(providerMessage).append("</a>");
        return sb.toString();
    }

    private SendPhoto createMessage(ServiceEntity service, Long chatId, String serviceInfo) {
        SendPhoto message = imageService.sendImageFromAWS(service.getTelegramId().toString(), service.getName());
        if (message == null) {
            return null;
        }
        message.setChatId(chatId);
        message.setCaption(serviceInfo);
        return message;
    }

    @Override
    public boolean execute(Update update) {
        String serviceId = update.getCallbackQuery().getData().split("/")[1];
        ServiceEntity service = serviceRepository.findFirstById(Long.parseLong(serviceId));
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        if (service.getTelegramId().equals(chatId)) {
            sendServiceInfoToCreator(service);
        } else {
            sendServiceInfo(service, chatId);
        }
        return true;
    }
}