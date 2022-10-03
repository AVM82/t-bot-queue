package ua.shpp.eqbot.cache;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.dto.ServiceDTO;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceCache {
    public static boolean justRegistrated = false;

    private static final Map<Long, ServiceDTO> services = new HashMap<>();

    private ServiceCache() {
    }

    public static void add(ServiceDTO serviceDTO) {
        if (serviceDTO.getTelegramId() != null) {
            services.put(serviceDTO.getTelegramId(), serviceDTO);
        }
    }

    public static void remove(ServiceDTO serviceDTO) {
        services.remove(serviceDTO.getTelegramId());
    }

    public static ServiceDTO findBy(Long id) {
        return services.get(id);
    }

    public static Map<Long, ServiceDTO> getAll() {
        return services;
    }
}
