package ua.shpp.eqbot.dto;

import ua.shpp.eqbot.stage.PositionRegistrationService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ServiceDTO {
    @NotNull(message = "{valid.service.telegramId.notnull.message}")
    private Long telegramId;
    @Size(min = 3, max = 50, message = "{valid.service.name.size.message}")
    private String name;
    private String description;
    private PositionRegistrationService positionRegistrationService;
    private String sundayWorkingHours;
    private String mondayWorkingHours;
    private String tuesdayWorkingHours;
    private String wednesdayWorkingHours;
    private String thursdayWorkingHours;
    private String fridayWorkingHours;
    private String saturdayWorkingHours;
    private String timeBetweenClients;

    public String getSundayWorkingHours() {
        return sundayWorkingHours;
    }

    public ServiceDTO setSundayWorkingHours(String sundayWorkingHours) {
        this.sundayWorkingHours = sundayWorkingHours;
        return this;
    }

    public String getMondayWorkingHours() {
        return mondayWorkingHours;
    }

    public ServiceDTO setMondayWorkingHours(String mondayWorkingHours) {
        this.mondayWorkingHours = mondayWorkingHours;
        return this;
    }

    public String getTuesdayWorkingHours() {
        return tuesdayWorkingHours;
    }

    public ServiceDTO setTuesdayWorkingHours(String tuesdayWorkingHours) {
        this.tuesdayWorkingHours = tuesdayWorkingHours;
        return this;
    }

    public String getWednesdayWorkingHours() {
        return wednesdayWorkingHours;
    }

    public ServiceDTO setWednesdayWorkingHours(String wednesdayWorkingHours) {
        this.wednesdayWorkingHours = wednesdayWorkingHours;
        return this;
    }

    public String getThursdayWorkingHours() {
        return thursdayWorkingHours;
    }

    public ServiceDTO setThursdayWorkingHours(String startWorkInThursday) {
        this.thursdayWorkingHours = startWorkInThursday;
        return this;
    }

    public String getFridayWorkingHours() {
        return fridayWorkingHours;
    }

    public ServiceDTO setFridayWorkingHours(String startWorkInFriday) {
        this.fridayWorkingHours = startWorkInFriday;
        return this;
    }

    public String getSaturdayWorkingHours() {
        return saturdayWorkingHours;
    }

    public ServiceDTO setSaturdayWorkingHours(String startWorkInSaturday) {
        this.saturdayWorkingHours = startWorkInSaturday;
        return this;
    }

    public String getTimeBetweenClients() {
        return timeBetweenClients;
    }

    public ServiceDTO setTimeBetweenClients(String timeBetweenClients) {
        this.timeBetweenClients = timeBetweenClients;
        return this;
    }

    public PositionRegistrationService getPositionRegistrationService() {
        return positionRegistrationService;
    }

    public ServiceDTO setPositionRegistrationService(PositionRegistrationService positionRegistrationService) {
        this.positionRegistrationService = positionRegistrationService;
        return this;
    }

    private byte[] avatar;

    public ServiceDTO() {
    }

    public ServiceDTO(Long telegramId, String name, String description) {
        this.telegramId = telegramId;
        this.name = name;
        this.description = description;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public ServiceDTO setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ServiceDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
