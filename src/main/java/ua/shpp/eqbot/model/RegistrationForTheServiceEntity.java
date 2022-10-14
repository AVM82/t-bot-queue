package ua.shpp.eqbot.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", nullable = false)
    private Set<UserEntity> users;

//    @OneToMany(fetch = FetchType.EAGER)
//    private Set<UserEntity> services;

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

//    public Set<UserEntity> getServices() {
//        return services;
//    }
//
//    public void setServices(Set<UserEntity> services) {
//        this.services = services;
//    }

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
