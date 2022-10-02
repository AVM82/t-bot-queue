package ua.shpp.eqbot.service.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.ServiceRestDTO;
import ua.shpp.eqbot.repository.ServiceRepository;

import java.util.List;

@Service
public class RestServiceService {

    @Autowired
    ServiceRepository repository;

    public List<ServiceEntity> getAllServices(){
        return repository.findAll();
    }

    public ServiceEntity getService(Long id){
        return repository.findFirstById(id);
    }

    public List<ServiceEntity> getAllServiceByIdTelegram(Long idTelegram){
        return repository.findAllByIdTelegram(idTelegram);
    }

    public void deleteAllServiceByIdTelegram(Long idTelegram){
        repository.deleteByIdTelegram(idTelegram);
    }

    public void postService (ServiceRestDTO serviceDTO){
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setAvatar(null);
        serviceEntity.setIdTelegram(serviceDTO.getIdTelegram());
        serviceEntity.setDescription(serviceDTO.getDescription());
        serviceEntity.setName(serviceDTO.getName());
        repository.save(serviceEntity);
    }

    public void deleteService(Long id){
        repository.deleteById(id);
    }
}
