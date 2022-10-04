package ua.shpp.eqbot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "appuser")
public class UserEntity {
   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;*/
   @Id
    private Long telegramId;
    private String name;
    private String city;
    private String phone;
    private String language;
    private LocalDateTime createdTime;

    public UserEntity() {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(telegramId, that.telegramId) && Objects.equals(name, that.name) && Objects.equals(city, that.city) && Objects.equals(phone, that.phone) && Objects.equals(language, that.language) && Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telegramId, name, city, phone, language, createdTime);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("telegramId=").append(telegramId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", language='").append(language).append('\'');
        sb.append(", timeCreated=").append(createdTime);
        sb.append('}');
        return sb.toString();
    }
}
