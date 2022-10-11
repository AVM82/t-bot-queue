package ua.shpp.eqbot.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RegistrationForTheServiceDto {
    @NotNull(message = "{valid.idService.notnull.message}")
    private Long serviceId;
    @NotNull(message = "{valid.idUser.notnull.message}")
    private Long userId;
    private LocalDateTime serviceRegistrationDateTime;

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

    public LocalDateTime getServiceRegistrationDateTime() {
        return serviceRegistrationDateTime;
    }

    public void setServiceRegistrationDateTime(LocalDateTime serviceRegistrationDateTime) {
        this.serviceRegistrationDateTime = serviceRegistrationDateTime;
    }
}
