package ua.shpp.eqbot.cache;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceCache {

    private static Map<Long, ServiceDTO> services = new HashMap<>();

    public static void add(ServiceDTO serviceDTO) {
        if (serviceDTO.getId_telegram() != null) {
            services.put(serviceDTO.getId_telegram(), serviceDTO);
        }
    }

    public static void remove(ServiceDTO serviceDTO) {
        services.remove(serviceDTO.getId_telegram());
    }

    public static ServiceDTO findBy(Long id) {
        return services.get(id);
    }

    public static List<ServiceDTO> getAll() {
        return new ArrayList<>(services.values());
    }
}
