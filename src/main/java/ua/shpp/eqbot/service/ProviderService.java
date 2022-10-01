package ua.shpp.eqbot.service;

import org.springframework.stereotype.Service;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProvideRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {
    private final ProvideRepository provideRepository;

    public ProviderService(ProvideRepository provideRepository) {
        this.provideRepository = provideRepository;
    }

    public ProviderEntity save(ProviderEntity providerEntity) {
        return provideRepository.save(providerEntity);
    }

    public Optional<ProviderEntity> get(Long id) {
        return provideRepository.findById(id);
    }

    public List<ProviderEntity> getByIdTelegram(Long idTelegram) {
        return provideRepository.findAllByIdTelegram(idTelegram);
    }

    public Optional<ProviderEntity> getByNameAndIdTelegram(Long id_provider, String city_provider) {
        return provideRepository.findProviderEntitiesByIdTelegramAndName(id_provider, city_provider);
    }

//    public ProviderEntity getByNameAndIdTelegram(Long idTelegram, String name) {
//        return provideRepository.findPleaseProviderEntitiesByIdTelegramAAndCity(idTelegram, name);
//    }

}
