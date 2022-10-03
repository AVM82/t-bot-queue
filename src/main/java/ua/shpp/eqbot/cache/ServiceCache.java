package ua.shpp.eqbot.cache;

import org.springframework.stereotype.Component;
import ua.shpp.eqbot.model.ServiceDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceCache {

    private static final Map<Long, ServiceDTO> services = new HashMap<>();

    private ServiceCache() {
    }

    public static void add(ServiceDTO serviceDTO) {
        if (serviceDTO.getIdTelegram() != null) {
            services.put(serviceDTO.getIdTelegram(), serviceDTO);
        }
    }

    public static void remove(ServiceDTO serviceDTO) {
        services.remove(serviceDTO.getIdTelegram());
    }
    public static void remove(Long id){
        if(findBy(id) != null)
            remove(findBy(id));
    }

    public static ServiceDTO findBy(Long id) {
        return services.get(id);
    }

    public static Map<Long, ServiceDTO> getAll() {
        return services;
    }
}
