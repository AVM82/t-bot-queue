package ua.shpp.eqbot.entity;


public class UserDto {
    private Long id;
    private String name;
    private String city;
    private String phone;
    private PositionRegistration positionRegistration;
    private PositionMenu positionMenu;

    public PositionRegistration getPositionRegistration() {
        return positionRegistration;
    }

    public UserDto setPositionRegistration(PositionRegistration positionRegistration) {
        this.positionRegistration = positionRegistration;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserDto setId(Long id) {
        this.id = id;
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

    public UserDto(){

    }


}
