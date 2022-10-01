package ua.shpp.eqbot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ProviderEntity {
    @Id

    private Long id;
    private Long idTelegram;
    private String name;
    private String city;

    public Long getIdTelegram() {
        return idTelegram;
    }

    public ProviderEntity setIdTelegram(Long idTelegram) {
        this.idTelegram = idTelegram;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProviderEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public ProviderEntity setCity(String city) {
        this.city = city;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProviderEntity that = (ProviderEntity) o;
        return Objects.equals(idTelegram, that.idTelegram) && Objects.equals(name, that.name) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTelegram, name, city);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProviderEntity{");
        sb.append("idTelegram=").append(idTelegram);
        sb.append(", name='").append(name).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
