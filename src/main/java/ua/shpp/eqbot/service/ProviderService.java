package ua.shpp.eqbot.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.shpp.eqbot.model.ProviderDto;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProvideRepository;

import java.util.Optional;

@Service
public class ProviderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderService.class);
    private final ProvideRepository provideRepository;
    private final String dtoCacheName = "cacheProviderDto";
    private final CacheManager cacheManager;
    private final ModelMapper modelMapper = new ModelMapper();

    public ProviderService(ProvideRepository provideRepository, CacheManager cacheManager) {
        this.provideRepository = provideRepository;
        this.cacheManager = cacheManager;
    }

    public ProviderEntity saveEntity(ProviderEntity providerEntity) {
        LOGGER.info("save provider entity {}", providerEntity);
        return provideRepository.save(providerEntity);
    }

    public void saveEntityInCache(ProviderEntity entity) {
        LOGGER.info("save provider entity in cache {}", entity);
        ProviderDto dto = convertToDto(entity);
        saveProviderDto(dto);
    }

    public void saveDtoInDataBase(ProviderDto providerDto) {
        LOGGER.info("save provider dto in database {}", providerDto);
        ProviderEntity entity= convertToEntity(providerDto);
        saveEntity(entity);
    }

    @CachePut(cacheNames = dtoCacheName, key = "#providerDto.idTelegram")
    public ProviderDto saveProviderDto(ProviderDto providerDto) {
        LOGGER.info("save provider dto {}", providerDto);
        return providerDto;
    }

    @Cacheable(cacheNames = dtoCacheName, key = "#id")
    public ProviderDto getProviderDto(Long id) {
        LOGGER.info("get provider dto by id {}", id);
        return null;
    }

    @Transactional
    @CacheEvict(cacheNames = dtoCacheName, key = "#id")
    public boolean remove(Long id) {
        LOGGER.info("delete provider dto and All entity");
        provideRepository.findAllByIdTelegram(id);
        return true;
    }

    public ProviderEntity getByIdTelegramEntity(Long idTelegram) {
        LOGGER.info("get entityProvider in database");
        return provideRepository.findByIdTelegram(idTelegram);
    }

    public void removeInDataBase(ProviderEntity entity){
        LOGGER.info("remove entityProvider in database");
        provideRepository.delete(entity);
    }

    public Optional<ProviderEntity> getByNameAndIdTelegram(Long id_provider, String city_provider) {
        return provideRepository.findProviderEntitiesByIdTelegramAndName(id_provider, city_provider);
    }

    private ProviderEntity convertToEntity(ProviderDto dto) {
        if (dto == null) return null;
        ProviderEntity entity = modelMapper.map(dto, ProviderEntity.class);
        LOGGER.info("convert dto to entity");
        return entity;
    }

    private ProviderDto convertToDto(ProviderEntity entity) {
        if (entity == null) return null;
        ProviderDto dto = modelMapper.map(entity, ProviderDto.class);
        LOGGER.info("convert entity to dto");
        return dto;
    }
}
