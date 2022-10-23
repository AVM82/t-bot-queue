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

    public RegistrationForTheServiceDto setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public RegistrationForTheServiceDto setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
        return this;
    }

    public LocalDateTime getServiceRegistrationDateTime() {
        return serviceRegistrationDateTime;
    }

    public RegistrationForTheServiceDto setServiceRegistrationDateTime(LocalDateTime serviceRegistrationDateTime) {
        this.serviceRegistrationDateTime = serviceRegistrationDateTime;
        return this;
    }

    @Override
    public String toString() {
        return "RegistrationForTheServiceDto{" + "userEntity= " + userEntity
                + ", serviceEntity= " + serviceEntity + '\''
                + ", serviceRegistrationDateTime='" + serviceRegistrationDateTime + '\''
                + '}';
    }
}
