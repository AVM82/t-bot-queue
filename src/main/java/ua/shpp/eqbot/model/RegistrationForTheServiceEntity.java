package ua.shpp.eqbot.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "registration_for_the_user")
public class RegistrationForTheServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "{valid.idService.notnull.message}")
    private Long serviceId;
    @NotNull(message = "{valid.idUser.notnull.message}")
    private Long userId;
    private LocalTime serviceRegistrationTime;
    private LocalDate serviceRegistrationDate;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long idService) {
        this.serviceId = idService;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long idUser) {
        this.userId = idUser;
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
