package ua.shpp.eqbot.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class RegistrationForTheServiceDto {
    @NotNull(message = "{valid.idService.notnull.message}")
    private Long serviceId;
    @NotNull(message = "{valid.idUser.notnull.message}")
    private Long userId;
    private LocalTime serviceRegistrationTime;
    private LocalDate serviceRegistrationDate;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalTime getServiceRegistrationTime() {
        return serviceRegistrationTime;
    }

    public void setServiceRegistrationTime(LocalTime serviceRegistrationTime) {
        this.serviceRegistrationTime = serviceRegistrationTime;
    }

    public LocalDate getServiceRegistrationDate() {
        return serviceRegistrationDate;
    }

    public void setServiceRegistrationDate(LocalDate serviceRegistrationDate) {
        this.serviceRegistrationDate = serviceRegistrationDate;
    }
}
