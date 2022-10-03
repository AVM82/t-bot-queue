package ua.shpp.eqbot.service;

import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public ProviderEntity save(ProviderEntity providerEntity) {
        return providerRepository.save(providerEntity);
    }

    public Optional<ProviderEntity> get(Long id) {
        return providerRepository.findById(id);
    }

    public List<ProviderEntity> getByIdTelegram(Long idTelegram) {
        return providerRepository.findAllByIdTelegram(idTelegram);
    }

    public Optional<ProviderEntity> getByNameAndIdTelegram(Long id_provider, String city_provider) {
        return providerRepository.findProviderEntitiesByIdTelegramAndName(id_provider, city_provider);
    }
}
