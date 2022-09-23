package ua.shpp.eqbot.entity;


public class UserEntity {
    private Long id;
    private String name;
    private String city;
    private String phone;
    private Position position;
    public Position getPosition() {
        return position;
    }

    public UserEntity setPosition(Position position) {
        this.position = position;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserEntity setId(Long id) {
        this.id = id;
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

    public UserEntity(){

    }
}
