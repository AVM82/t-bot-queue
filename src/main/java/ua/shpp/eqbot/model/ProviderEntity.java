package ua.shpp.eqbot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ProviderEntity {
    @Id
    private Long idTelegram;
    private String name;
    private String city;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setIdTelegram(Long id_telegram) {
        this.idTelegram = id_telegram;
    }

    public Long getIdTelegram() {
        return idTelegram;
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
        sb.append("id_telegram=").append(idTelegram);
        sb.append(", name='").append(name).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
