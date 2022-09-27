package ua.shpp.eqbot.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;

@Service
public class SaveService {
    Logger log = LoggerFactory.getLogger(SaveService.class);
    final ServiceRepository serviceRepository;

    public SaveService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public void addService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setId_telegram(service.getId_telegram()).setName(service.getName()).setDescription(service.getDescription());
        serviceRepository.save(serviceEntity);
    }

}
