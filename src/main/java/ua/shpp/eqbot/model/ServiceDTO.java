package ua.shpp.eqbot.model;

import ua.shpp.eqbot.stage.PositionRegistrationService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ServiceDTO {
    @NotNull(message = "{valid.service.idTelegram.notnull.message}")
    private Long idTelegram;
    @Size(min = 3, max = 50, message = "{valid.service.name.size.message}")
    private String name;

    private String description;

    private PositionRegistrationService positionRegistrationService;

    private String startWorkInSunday;
    private String endWorkInSunday;
    private String startWorkInMonday;
    private String endWorkInMonday;
    private String startWorkInTuesday;
    private String endWorkInTuesday;
    private String startWorkInWednesday;
    private String endWorkInwWednesday;
    private String startWorkInThursday;
    private String endWorkInThursday;
    private String startWorkInFriday;
    private String endWorkInFriday;
    private String startWorkInSaturday;
    private String endWorkInSaturday;
    private String timeBetweenClients;

    public String getStartWorkInSunday() {
        return startWorkInSunday;
    }

    public ServiceDTO setStartWorkInSunday(String startWorkInSunday) {
        this.startWorkInSunday = startWorkInSunday;
        return this;
    }

    public String getEndWorkInSunday() {
        return endWorkInSunday;
    }

    public ServiceDTO setEndWorkInSunday(String endWorkInSunday) {
        this.endWorkInSunday = endWorkInSunday;
        return this;
    }

    public String getStartWorkInMonday() {
        return startWorkInMonday;
    }

    public ServiceDTO setStartWorkInMonday(String startWorkInMonday) {
        this.startWorkInMonday = startWorkInMonday;
        return this;
    }

    public String getEndWorkInMonday() {
        return endWorkInMonday;
    }

    public ServiceDTO setEndWorkInMonday(String endWorkInMonday) {
        this.endWorkInMonday = endWorkInMonday;
        return this;
    }

    public String getStartWorkInTuesday() {
        return startWorkInTuesday;
    }

    public ServiceDTO setStartWorkInTuesday(String startWorkInTuesday) {
        this.startWorkInTuesday = startWorkInTuesday;
        return this;
    }

    public String getEndWorkInTuesday() {
        return endWorkInTuesday;
    }

    public ServiceDTO setEndWorkInTuesday(String endWorkInTuesday) {
        this.endWorkInTuesday = endWorkInTuesday;
        return this;
    }

    public String getStartWorkInWednesday() {
        return startWorkInWednesday;
    }

    public ServiceDTO setStartWorkInWednesday(String startWorkInWednesday) {
        this.startWorkInWednesday = startWorkInWednesday;
        return this;
    }

    public String getEndWorkInwWednesday() {
        return endWorkInwWednesday;
    }

    public ServiceDTO setEndWorkInwWednesday(String endWorkInwWednesday) {
        this.endWorkInwWednesday = endWorkInwWednesday;
        return this;
    }

    public String getStartWorkInThursday() {
        return startWorkInThursday;
    }

    public ServiceDTO setStartWorkInThursday(String startWorkInThursday) {
        this.startWorkInThursday = startWorkInThursday;
        return this;
    }

    public String getEndWorkInThursday() {
        return endWorkInThursday;
    }

    public ServiceDTO setEndWorkInThursday(String endWorkInThursday) {
        this.endWorkInThursday = endWorkInThursday;
        return this;
    }

    public String getStartWorkInFriday() {
        return startWorkInFriday;
    }

    public ServiceDTO setStartWorkInFriday(String startWorkInFriday) {
        this.startWorkInFriday = startWorkInFriday;
        return this;
    }

    public String getEndWorkInFriday() {
        return endWorkInFriday;
    }

    public ServiceDTO setEndWorkInFriday(String endWorkInFriday) {
        this.endWorkInFriday = endWorkInFriday;
        return this;
    }

    public String getStartWorkInSaturday() {
        return startWorkInSaturday;
    }

    public ServiceDTO setStartWorkInSaturday(String startWorkInSaturday) {
        this.startWorkInSaturday = startWorkInSaturday;
        return this;
    }

    public String getEndWorkInSaturday() {
        return endWorkInSaturday;
    }

    public ServiceDTO setEndWorkInSaturday(String endWorkInSaturday) {
        this.endWorkInSaturday = endWorkInSaturday;
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

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Long getIdTelegram() {
        return idTelegram;
    }

    public ServiceDTO setIdTelegram(Long idTelegram) {
        this.idTelegram = idTelegram;
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
