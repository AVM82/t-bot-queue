package ua.shpp.eqbot.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.shpp.eqbot.dto.ProviderDto;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.stage.PositionRegistrationProvider;

import java.util.HashSet;


@Service
public class ProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderService.class);
    private final ProviderRepository providerRepository;
    private static final String PROVIDER_DTO_CACHE_NAME = "cacheProviderDto";
    private final ModelMapper modelMapper = new ModelMapper();

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public ProviderEntity saveEntity(ProviderEntity providerEntity) {
        LOGGER.info("save provider entity {}", providerEntity);
        return providerRepository.save(providerEntity);
    }

    public void saveEntityInCache(ProviderEntity entity) {
        LOGGER.info("save provider entity in cache {}", entity);
        ProviderDto dto = convertToDto(entity);
        dto.setPositionRegistrationProvider(PositionRegistrationProvider.DONE);
        saveProviderDto(dto);
    }

    public void saveDtoInDataBase(ProviderDto providerDto) {
        LOGGER.info("save provider dto in database {}", providerDto);
        ProviderEntity entity = convertToEntity(providerDto);
        saveEntity(entity);
    }

    @CachePut(cacheNames = PROVIDER_DTO_CACHE_NAME, key = "#providerDto.telegramId")
    public ProviderDto saveProviderDto(ProviderDto providerDto) {
        LOGGER.info("save provider dto {}", providerDto);
        return providerDto;
    }


    @Cacheable(cacheNames = PROVIDER_DTO_CACHE_NAME, key = "#telegramId")
    public ProviderDto getProviderDto(Long telegramId) {
        LOGGER.info("get provider dto by telegramId {}", telegramId);
        ProviderEntity providerEntity = getByTelegramIdEntity(telegramId);
        ProviderDto providerDto = convertToDto(providerEntity);
        if (providerDto != null) {
            providerDto.setPositionRegistrationProvider(PositionRegistrationProvider.DONE);
        }
        return providerDto;
    }

    @Transactional
    @CacheEvict(cacheNames = PROVIDER_DTO_CACHE_NAME, key = "#id")
    public boolean remove(Long id) {
        LOGGER.info("delete provider dto and All entity");
        ProviderEntity entity = providerRepository.findByTelegramId(id);
        if (entity != null) {
            providerRepository.delete(entity);
            return true;
        } else {
            return false;
        }

    }

    public ProviderEntity getByTelegramIdEntity(Long telegramId) {
        LOGGER.info("get entityProvider in database");
        return providerRepository.findByTelegramId(telegramId);
    }

    public void removeInDataBase(ProviderEntity entity) {
        LOGGER.info("remove entityProvider in database");
        providerRepository.delete(entity);
    }

    private ProviderEntity convertToEntity(ProviderDto dto) {
        if (dto == null) {
            return null;
        }
        ProviderEntity entity = modelMapper.map(dto, ProviderEntity.class);
        LOGGER.info("convert dto to entity");
        return entity;
    }

    private ProviderDto convertToDto(ProviderEntity entity) {
        if (entity == null) {
            return null;
        }
        ProviderDto dto = modelMapper.map(entity, ProviderDto.class);
        LOGGER.info("convert entity to dto");
        return dto;
    }
}
