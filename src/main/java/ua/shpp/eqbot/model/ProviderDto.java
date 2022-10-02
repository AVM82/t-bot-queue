package ua.shpp.eqbot.model;

import ua.shpp.eqbot.stage.PositionRegistrationProvider;

public class ProviderDto {
    private Long idTelegram;
    private String name;
    private String city;
    private PositionRegistrationProvider positionRegistrationProvider;

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
    public String toString() {
        return "ProviderEntity{" +
                ", idTelegram =" + idTelegram +
                ", name ='" + name + '\'' +
                ", city ='" + city + '\'' +
                '}';
    }
}
