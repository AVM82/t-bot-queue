package ua.shpp.eqbot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ProviderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idTelegram;
    private String name;
    private String city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setIdTelegram(Long idTelegram) {
        this.idTelegram = idTelegram;
    }

    public Long getIdTelegram() {
        return idTelegram;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProviderEntity that = (ProviderEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idTelegram != null ? !idTelegram.equals(that.idTelegram) : that.idTelegram != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return city != null ? city.equals(that.city) : that.city == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idTelegram != null ? idTelegram.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProviderEntity{" +
                "id=" + id +
                ", idTelegram=" + idTelegram +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }


}
