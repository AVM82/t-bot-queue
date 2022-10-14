package ua.shpp.eqbot.service.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.RegistrationForTheServiceEntity;
import ua.shpp.eqbot.repository.RegistrationForTheServiceRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderBeforeVisit {
    static final Logger LOGGER = LoggerFactory.getLogger(ReminderBeforeVisit.class);

    final RegistrationForTheServiceRepository registrationForTheServiceRepository;

    public ReminderBeforeVisit(RegistrationForTheServiceRepository registrationForTheServiceRepository) {
        this.registrationForTheServiceRepository = registrationForTheServiceRepository;
    }

    @Scheduled(cron = "${reminder.interval-cron}")
    @Async
    public void rem() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(2);
        List<RegistrationForTheServiceEntity> firstQuery = registrationForTheServiceRepository.findAllBetweenDates(now, tomorrow);
        for(RegistrationForTheServiceEntity entity : firstQuery) {
            entity.getServiceId();
            entity.getUserId();
//            entity.getServices();
            entity.getUsers();
        }
    }
}
