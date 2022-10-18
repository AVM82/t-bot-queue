package ua.shpp.eqbot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registration_for_the_user")
public class RegistrationForTheServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime serviceRegistrationDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity serviceEntity;

    private LocalDateTime dateReminderSent;

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

    public void setServiceRegistrationDateTime(LocalDateTime serviceRegistrationTime) {
        this.serviceRegistrationDateTime = serviceRegistrationTime;
    }

    public LocalDateTime getDateReminderSent() {
        return dateReminderSent;
    }

    public void setDateReminderSent(LocalDateTime dateReminderSent) {
        this.dateReminderSent = dateReminderSent;
    }
}
