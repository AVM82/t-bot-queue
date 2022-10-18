package ua.shpp.eqbot.service.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.model.RegistrationForTheServiceEntity;
import ua.shpp.eqbot.repository.RegistrationForTheServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReminderBeforeVisit {
    static final Logger LOGGER = LoggerFactory.getLogger(ReminderBeforeVisit.class);
    final RegistrationForTheServiceRepository registrationForTheServiceRepository;
    final SendBotMessageService sendBotMessageService;
    final BundleLanguage bundleLanguage;
    List<RegistrationForTheServiceEntity> firstQuery = null;

    public ReminderBeforeVisit(RegistrationForTheServiceRepository registrationForTheServiceRepository,
                               SendBotMessageService sendBotMessageService,
                               BundleLanguage bundleLanguage) {
        this.registrationForTheServiceRepository = registrationForTheServiceRepository;
        this.sendBotMessageService = sendBotMessageService;
        this.bundleLanguage = bundleLanguage;
    }

    @Scheduled(cron = "${reminder.interval-cron}")
    @Async
    public void reminderOneDayBeforeVisit() {
        LOGGER.info("Start reminder before visit.");
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime tomorrow = now.plusDays(2);
        String text;

        firstQuery = registrationForTheServiceRepository.findAllBetweenDates(now, tomorrow);
        for (RegistrationForTheServiceEntity entity : firstQuery) {
            text = bundleLanguage.getValue(entity.getUserEntity().getId(), "reminder.text1") + " "
                    + entity.getServiceRegistrationDateTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy "))
                    + bundleLanguage.getValue(entity.getUserEntity().getId(), "reminder.text2") + " \""
                    + entity.getServiceEntity().getName() + "\"";
            sendBotMessageService.sendMessage(String.valueOf(entity.getUserEntity().getTelegramId()), text);
            LOGGER.info("A reminder has been sent to the user with id {}", entity.getUserEntity().getTelegramId());
            entity.setSentReminderDate(now);
        }
        LOGGER.info("Message sent to {} users.", firstQuery.size());
        registrationForTheServiceRepository.saveAll(firstQuery);
        firstQuery = null;
    }
}
