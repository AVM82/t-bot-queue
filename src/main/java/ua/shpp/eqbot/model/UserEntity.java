package ua.shpp.eqbot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "appuser")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_telegram")
    private Long idTelegram;
    private String name;
    private String city;
    private String phone;
    private String language;
    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    public UserEntity() {
    }

    @PrePersist
    private void setTime() {
        timeCreated = LocalDateTime.now();
    }

    public Long getIdTelegram() {
        return idTelegram;
    }

    public UserEntity setIdTelegram(Long idTelegram) {
        this.idTelegram = idTelegram;
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

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(idTelegram, that.idTelegram) && Objects.equals(name, that.name) && Objects.equals(city, that.city) && Objects.equals(phone, that.phone) && Objects.equals(language, that.language) && Objects.equals(timeCreated, that.timeCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTelegram, name, city, phone, language, timeCreated);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("idTelegram=").append(idTelegram);
        sb.append(", name='").append(name).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", language='").append(language).append('\'');
        sb.append(", timeCreated=").append(timeCreated);
        sb.append('}');
        return sb.toString();
    }
}
