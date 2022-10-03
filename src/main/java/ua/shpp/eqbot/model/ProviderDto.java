package ua.shpp.eqbot.model;

import ua.shpp.eqbot.stage.PositionRegistrationProvider;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class ProviderDto implements Serializable {
    @NotNull(message = "{valid.provider.id.notnull.message}")
    private Long idTelegram;
    @Size(min = 3, max = 30, message = "{valid.provider.name.size.message}")
    private String name;
    @Size(min = 3, max = 20, message = "{valid.provider.city.size.message}")
    private String city;
    private PositionRegistrationProvider positionRegistrationProvider;

    public ProviderDto(Long idTelegram, String name, String city) {
        this.idTelegram = idTelegram;
        this.name = name;
        this.city = city;
    }

    public ProviderDto(){
    }

    public Long getIdTelegram() {
        return idTelegram;
    }

    public ProviderDto setIdTelegram(Long idTelegram) {
        this.idTelegram = idTelegram;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProviderDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public ProviderDto setCity(String city) {
        this.city = city;
        return this;
    }

    public PositionRegistrationProvider getPositionRegistrationProvider() {
        return positionRegistrationProvider;
    }

    public ProviderDto setPositionRegistrationProvider(PositionRegistrationProvider positionRegistrationProvider) {
        this.positionRegistrationProvider = positionRegistrationProvider;
        return this;
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
