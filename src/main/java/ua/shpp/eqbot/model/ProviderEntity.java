package ua.shpp.eqbot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ProviderEntity {
    @Id
    private Long id_telegram;
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

    public void setId_telegram(Long id_telegram) {
        this.id_telegram = id_telegram;
    }

    public Long getId_telegram() {
        return id_telegram;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProviderEntity that = (ProviderEntity) o;
        return Objects.equals(id_telegram, that.id_telegram) && Objects.equals(name, that.name) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_telegram, name, city);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProviderEntity{");
        sb.append("id_telegram=").append(id_telegram);
        sb.append(", name='").append(name).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
