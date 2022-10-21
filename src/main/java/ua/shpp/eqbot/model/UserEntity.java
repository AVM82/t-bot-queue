package ua.shpp.eqbot.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "appuser")
public class UserEntity {

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userEntity")
    Set<RegistrationForTheServiceEntity> registrationForTheServiceEntities;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramId;
    @NotBlank
    private String name;
    private String city;
    private String phone;
    private String language;
    private LocalDateTime createdTime;

    public UserEntity() {
    }

    public UserEntity(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    @PrePersist
    private void setTime() {
        createdTime = LocalDateTime.now();
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public UserEntity setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserEntity setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserEntity setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public UserEntity setLanguage(String language) {
        this.language = language;
        return this;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime timeCreated) {
        this.createdTime = timeCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null
                || getClass() != o.getClass()) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return Objects.equals(telegramId, that.telegramId)
                && Objects.equals(name, that.name)
                && Objects.equals(city, that.city)
                && Objects.equals(phone, that.phone)
                && Objects.equals(language, that.language)
                && Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telegramId, name, city, phone, language, createdTime);
    }

    @Override
    public String toString() {
        return "UserEntity{"
                + "telegramId=" + telegramId
                + ", name='" + name + '\''
                + ", city='" + city + '\''
                + ", phone='" + phone + '\''
                + ", language='" + language + '\''
                + ", timeCreated=" + createdTime + '}';
    }

    public Set<RegistrationForTheServiceEntity> getRegistrationForTheServiceEntities() {
        return registrationForTheServiceEntities;
    }

    public void setRegistrationForTheServiceEntities(Set<RegistrationForTheServiceEntity> registrationForTheServiceEntities) {
        this.registrationForTheServiceEntities = registrationForTheServiceEntities;
    }
}
