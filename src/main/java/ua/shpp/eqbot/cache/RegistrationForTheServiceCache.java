package ua.shpp.eqbot.cache;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.dto.RegistrationForTheServiceDto;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegistrationForTheServiceCache {
    private static final Map<Long, RegistrationForTheServiceDto> SERVICES = new HashMap<>();

    private RegistrationForTheServiceCache() {
    }

    public static void add(RegistrationForTheServiceDto registrationForTheServiceDto, Long telegramId) {
        if (findByUserTelegramId(telegramId) == null) {
            SERVICES.put(telegramId, registrationForTheServiceDto);
        }
    }

    public static void remove(Long telegramId) {
        SERVICES.remove(telegramId);
    }

    public static RegistrationForTheServiceDto findByUserTelegramId(Long id) {
        return SERVICES.get(id);
    }
}
