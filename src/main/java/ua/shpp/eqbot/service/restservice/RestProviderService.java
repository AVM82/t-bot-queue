package ua.shpp.eqbot.service.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.ProviderDto;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;

import java.util.List;

@Service
public class RestProviderService {

    @Autowired
    ProviderRepository repository;

    public List<ProviderEntity> getAllProviders(){
        return repository.findAll();
    }

    public List<ProviderEntity> getProviders(Long idTelegram){
        return repository.findAllByIdTelegram(idTelegram);
    }

    public ProviderEntity getProvider(Long idTelegram){
        return repository.findByIdTelegram(idTelegram);
    }

    public void postProvider(ProviderDto providerDto){
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setIdTelegram(providerDto.getIdTelegram());
        providerEntity.setCity(providerDto.getCity());
        providerEntity.setName(providerDto.getName());
        repository.save(providerEntity);
    }

    public void deleteProvider(Long idTelegram){
        repository.deleteById(idTelegram);
    }
}
