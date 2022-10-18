package ua.shpp.eqbot.dto;

import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserEntity;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RegistrationForTheServiceDto {
    @NotNull
    private UserEntity userEntity;
    @NotNull
    private ServiceEntity serviceEntity;
    private LocalDateTime serviceRegistrationDateTime;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }

    public LocalDateTime getServiceRegistrationDateTime() {
        return serviceRegistrationDateTime;
    }

    public void setServiceRegistrationDateTime(LocalDateTime serviceRegistrationDateTime) {
        this.serviceRegistrationDateTime = serviceRegistrationDateTime;
    }
}
