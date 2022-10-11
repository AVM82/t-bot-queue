package ua.shpp.eqbot.dto;

import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.PositionRegistration;

public class UserDto {

    private Long telegramId;
    private String name;
    private String city;
    private String phone;
    private String language;
    private PositionRegistration positionRegistration;
    private PositionMenu positionMenu;

    public UserDto() {
    }

    public UserDto(Long telegramId, String name) {
        this.telegramId = telegramId;
        this.name = name;
    }

    public UserDto(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public PositionRegistration getPositionRegistration() {
        return positionRegistration;
    }

    public UserDto setPositionRegistration(PositionRegistration positionRegistration) {
        this.positionRegistration = positionRegistration;
        return this;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public UserDto setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDto{");
        sb.append("telegramId=").append(telegramId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", language='").append(language).append('\'');
        sb.append(", positionRegistration=").append(positionRegistration);
        sb.append(", positionMenu=").append(positionMenu);
        sb.append('}');
        return sb.toString();
    }
}
