package ua.shpp.eqbot.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class UserRestDto implements Serializable {
    @NotNull(message = "{valid.user.id.notnull.message}")
    private final Long telegramId;
    @Size(min = 3, max = 30, message = "{valid.name.id.size.message}")
    private final String name;
    @Size(min = 3, max = 30, message = "{valid.user.city.size.message}")
    private final String city;
    @Pattern(regexp = "^[+]?[(]?\\d{3}[ )]?[-\\s.]?\\d{3}[-\\s.]?\\d{4,6}$",
            message = "{valid.user.phone.pattern.message}")
    private final String phone;
    private final String language;

    public UserRestDto(Long telegramId, String name, String city, String phone, String language) {
        this.telegramId = telegramId;
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.language = language;
    }

    public Long getTelegramId() {
        return telegramId;
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
        if (this == o) {
            return true;
        }
        if (o == null
                || getClass() != o.getClass()) {
            return false;
        }
        UserRestDto entity = (UserRestDto) o;
        return Objects.equals(this.telegramId, entity.telegramId)
                && Objects.equals(this.name, entity.name)
                && Objects.equals(this.city, entity.city)
                && Objects.equals(this.phone, entity.phone)
                && Objects.equals(this.language, entity.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telegramId, name, city, phone, language);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "("
                + "telegramId = " + telegramId + ", "
                + "name = " + name + ", "
                + "city = " + city + ", "
                + "phone = " + phone + ", "
                + "language = " + language + ")";
    }
}
