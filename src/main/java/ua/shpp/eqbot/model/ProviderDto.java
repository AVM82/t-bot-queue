package ua.shpp.eqbot.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class ProviderDto implements Serializable {
    @NotNull(message = "{valid.provider.id.notnull.message}")
    private final Long idTelegram;
    @Size(min = 3, max = 30, message = "{valid.provider.name.size.message}")
    private final String name;
    @Size(min = 3, max = 20, message = "{valid.provider.city.size.message}")
    private final String city;

    public ProviderDto(Long idTelegram, String name, String city) {
        this.idTelegram = idTelegram;
        this.name = name;
        this.city = city;
    }

    public Long getIdTelegram() {
        return idTelegram;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProviderDto entity = (ProviderDto) o;
        return Objects.equals(this.idTelegram, entity.idTelegram) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.city, entity.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTelegram, name, city);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "idTelegram = " + idTelegram + ", " +
                "name = " + name + ", " +
                "city = " + city + ")";
    }
}
