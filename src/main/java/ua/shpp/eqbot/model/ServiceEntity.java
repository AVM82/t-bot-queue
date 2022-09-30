package ua.shpp.eqbot.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "services")
public class ServiceEntity {

    @Id
    private Long idTelegram;
    private String name;
    private String description;

    @Lob
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


    public Long getIdTelegram() {
        return idTelegram;
    }

    public ServiceEntity setIdTelegram(Long id_telegram) {
        this.idTelegram = id_telegram;
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
        return Objects.equals(idTelegram, that.idTelegram) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Arrays.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idTelegram, name, description);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceEntity{");
        sb.append("id_telegram=").append(idTelegram);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", avatar=").append(Arrays.toString(avatar));
        sb.append('}');
        return sb.toString();
    }
}
