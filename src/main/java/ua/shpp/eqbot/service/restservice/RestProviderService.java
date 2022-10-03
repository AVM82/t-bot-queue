package ua.shpp.eqbot.service.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.dto.ProviderDto;
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

    public List<ProviderEntity> getProviders(Long telegramId){
        return repository.findAllByTelegramId(telegramId);
    }

    public ProviderEntity getProvider(Long telegramId){
        return repository.findByTelegramId(telegramId);
    }

    public void postProvider(ProviderDto providerDto){
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setTelegramId(providerDto.getTelegramId());
        providerEntity.setProviderCity(providerDto.getCity());
        providerEntity.setName(providerDto.getName());
        repository.save(providerEntity);
    }

    public void deleteProvider(Long telegramId){
        repository.deleteById(telegramId);
    }
}
