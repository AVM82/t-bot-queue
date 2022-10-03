package ua.shpp.eqbot.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "services")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramId;
    private String name;
    private String description;

    @Type(type = "org.hibernate.type.BinaryType")
    @Column(columnDefinition = "bytea")
    private byte[] avatar;

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public ServiceEntity() {
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public ServiceEntity setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ServiceEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceEntity that = (ServiceEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (telegramId != null ? !telegramId.equals(that.telegramId) : that.telegramId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return Arrays.equals(avatar, that.avatar);
    }

    @Override
    public String toString() {
        return "ServiceEntity{" +
                "id=" + id +
                ", telegramId=" + telegramId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", avatar=" + Arrays.toString(avatar) +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (telegramId != null ? telegramId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }
}
