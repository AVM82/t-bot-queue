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

    private LocalDateTime sentReminderDate;

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

    public LocalDateTime getSentReminderDate() {
        return sentReminderDate;
    }

    public void setSentReminderDate(LocalDateTime sentReminderDate) {
        this.sentReminderDate = sentReminderDate;
    }

    @Override
    public String toString() {
        return "RegistrationForTheServiceEntity{" + "userEntity= " + userEntity
                + ", serviceEntity= " + serviceEntity + '\''
                + ", serviceRegistrationDateTime='" + serviceRegistrationDateTime + '\''
                + ", sentReminderDate='" + sentReminderDate + '\''
                + '}';
    }
}
