package ua.shpp.eqbot.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.dto.ServiceDTO;


@Component
public class ServiceCache {

    private static final Map<Long, ServiceDTO> SERVICES = new HashMap<>();

    private ServiceCache() {
    }

    public static void add(ServiceDTO serviceDTO) {
        if (serviceDTO.getTelegramId() != null) {
            SERVICES.put(serviceDTO.getTelegramId(), serviceDTO);
        }
    }

    public static void remove(ServiceDTO serviceDTO) {
        SERVICES.remove(serviceDTO.getTelegramId());
    }

    public static void remove(Long id) {
        if (findBy(id) != null) {
            remove(findBy(id));
        }
    }

    public static ServiceDTO findBy(Long id) {
        return SERVICES.get(id);
    }

    public static Map<Long, ServiceDTO> getAll() {
        return SERVICES;
    }
}
