package ua.shpp.eqbot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.shpp.eqbot.dto.RegistrationForTheServiceDto;
import ua.shpp.eqbot.model.RegistrationForTheServiceEntity;

@Mapper
public interface RegistrationForTheServiceMapper {
    RegistrationForTheServiceMapper INSTANCE = Mappers.getMapper(RegistrationForTheServiceMapper.class);

    RegistrationForTheServiceEntity registrationForTheServiceDtoToEntity(RegistrationForTheServiceDto dto);
}
