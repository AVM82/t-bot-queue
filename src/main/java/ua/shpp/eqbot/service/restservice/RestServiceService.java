package ua.shpp.eqbot.service.restservice;

import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.dto.ServiceRestDTO;
import ua.shpp.eqbot.repository.ServiceRepository;

import java.util.List;

@Service
public class RestServiceService {

    final ServiceRepository repository;

    public RestServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    public List<ServiceEntity> getAllServices() {
        return repository.findAll();
    }

    public ServiceEntity getService(Long id) {
        return repository.findFirstById(id);
    }

    public List<ServiceEntity> getAllServiceByTelegramId(Long telegramId) {
        return repository.findAllByTelegramId(telegramId);
    }

    public void postService(ServiceRestDTO serviceDTO) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setAvatar(null);
        serviceEntity.setTelegramId(serviceDTO.getTelegramId());
        serviceEntity.setDescription(serviceDTO.getDescription());
        serviceEntity.setName(serviceDTO.getName());
        repository.save(serviceEntity);
    }

    public void deleteService(Long id) {
        repository.deleteById(id);
    }
}
