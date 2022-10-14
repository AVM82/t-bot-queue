package ua.shpp.eqbot.service.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.RegistrationForTheServiceEntity;
import ua.shpp.eqbot.repository.RegistrationForTheServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderBeforeVisit {
    static final Logger LOGGER = LoggerFactory.getLogger(ReminderBeforeVisit.class);
    final RegistrationForTheServiceRepository registrationForTheServiceRepository;
    final SendBotMessageService sendBotMessageService;
    List<RegistrationForTheServiceEntity> firstQuery = null;

    public ReminderBeforeVisit(RegistrationForTheServiceRepository registrationForTheServiceRepository, SendBotMessageService sendBotMessageService) {
        this.registrationForTheServiceRepository = registrationForTheServiceRepository;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Scheduled(cron = "${reminder.interval-cron}")
    @Async
    public void reminderOneDayBeforeVisit() {
        LOGGER.info("Start reminder before visit.");
        String text;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        firstQuery = registrationForTheServiceRepository.findAllBetweenDates(now, tomorrow);
        LOGGER.info("");
        for (RegistrationForTheServiceEntity entity : firstQuery) {
            text = "Ви записані на "
                    + entity.getServiceRegistrationDateTime()
                    + "на послугу \""
                    + entity.getServiceEntity().getName() + "\"";
            sendBotMessageService.sendMessage(String.valueOf(entity.getUserEntity().getTelegramId()), text);
            LOGGER.info("A reminder has been sent to the user with id {}", entity.getUserEntity().getTelegramId());
        }
        LOGGER.info("Message sent to {} users.", firstQuery.size());
        firstQuery = null;
    }
}
