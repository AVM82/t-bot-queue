package ua.shpp.eqbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;

    @Autowired
    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public ProviderEntity save(ProviderEntity providerEntity) {
        return providerRepository.save(providerEntity);
    }

    public Optional<ProviderEntity> get(Long id) {
        return providerRepository.findById(id);
    }

    public List<ProviderEntity> getByTelegramId(Long telegramId) {
        return providerRepository.findAllByTelegramId(telegramId);
    }

    public Optional<ProviderEntity> getByNameAndTelegramId(Long providerId, String providerCity) {
        return providerRepository.findProviderEntitiesByTelegramIdAndProviderCity(providerId, providerCity);
    }

    public void remove(Long telegramId) {
        Optional<ProviderEntity> providerEntity = get(telegramId);
        providerEntity.ifPresent(providerRepository::delete);
    }
}
