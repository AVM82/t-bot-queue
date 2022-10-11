package ua.shpp.eqbot.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class RegistrationForTheServiceDto {
    @NotNull(message = "{valid.idService.notnull.message}")
    private Long idService;
    @NotNull(message = "{valid.idUser.notnull.message}")
    private Long idUser;
    private LocalTime serviceRegistrationTime;
    private LocalDate serviceRegistrationDate;

    public Long getIdService() {
        return idService;
    }

    public void setIdService(Long idService) {
        this.idService = idService;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
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
