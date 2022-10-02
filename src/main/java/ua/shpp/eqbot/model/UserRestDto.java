package ua.shpp.eqbot.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class UserRestDto implements Serializable {
    @NotNull(message = "{valid.user.id.notnull.message}")
    private final Long idTelegram;
    @Size(min = 3, max = 30, message = "{valid.name.id.size.message}")
    private final String name;
    @Size(min = 3, max = 30, message = "{valid.user.city.size.message}")
    private final String city;
    @Pattern(regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$",
    message = "{valid.user.phone.pattern.message}")
    private final String phone;
    private final String language;

    public UserRestDto(Long idTelegram, String name, String city, String phone, String language) {
        this.idTelegram = idTelegram;
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.language = language;
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

    public String getPhone() {
        return phone;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRestDto entity = (UserRestDto) o;
        return Objects.equals(this.idTelegram, entity.idTelegram) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.city, entity.city) &&
                Objects.equals(this.phone, entity.phone) &&
                Objects.equals(this.language, entity.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTelegram, name, city, phone, language);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "idTelegram = " + idTelegram + ", " +
                "name = " + name + ", " +
                "city = " + city + ", " +
                "phone = " + phone + ", " +
                "language = " + language + ")";
    }
}
