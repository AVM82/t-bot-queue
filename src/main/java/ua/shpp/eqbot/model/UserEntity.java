package ua.shpp.eqbot.model;

import javax.persistence.*;
@Entity
@Table(name = "appuser")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long id_telegram;
    private String name;
    private String city;
    private String phone;
    private String language;

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public UserEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getId_telegram() {
        return id_telegram;
    }

    public UserEntity setId_telegram(Long id_telegram) {
        this.id_telegram = id_telegram;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserEntity setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserEntity setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public UserEntity setLanguage(String language) {
        this.language = language;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (id_telegram != null ? !id_telegram.equals(that.id_telegram) : that.id_telegram != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        return phone != null ? phone.equals(that.phone) : that.phone == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (id_telegram != null ? id_telegram.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", id_telegram=" + id_telegram +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("id_telegram=").append(id_telegram);
        sb.append(", name='").append(name).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", language='").append(language).append('\'');
        sb.append(", timeCreated=").append(timeCreated);
        sb.append('}');
        return sb.toString();
    }
}
