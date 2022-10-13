package ua.shpp.eqbot.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    private LocalDateTime serviceRegistrationDateTime;

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

    public LocalDateTime getServiceRegistrationDateTime() {
        return serviceRegistrationDateTime;
    }

    public void setServiceRegistrationDateTime(LocalDateTime serviceRegistrationTime) {
        this.serviceRegistrationDateTime = serviceRegistrationTime;
    }
}