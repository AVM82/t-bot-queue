package ua.shpp.eqbot.model;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {

    private Long idTelegram;

    private String name;
    private String city;
    private String phone;
    private String language;
    private PositionRegistration positionRegistration;
    private PositionMenu positionMenu;

    public PositionRegistration getPositionRegistration() {
        return positionRegistration;
    }

    public UserDto setPositionRegistration(PositionRegistration positionRegistration) {
        this.positionRegistration = positionRegistration;
        return this;
    }

    public Long getIdTelegram() {
        return idTelegram;
    }

    public UserDto setIdTelegram(Long idTelegram) {
        this.idTelegram = idTelegram;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserDto setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public PositionMenu getPositionMenu() {
        return positionMenu;
    }

    public UserDto setPositionMenu(PositionMenu positionMenu) {
        this.positionMenu = positionMenu;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public UserDto setLanguage(String language) {
        this.language = language;
        return this;
    }

    public UserDto() {
    }
}
