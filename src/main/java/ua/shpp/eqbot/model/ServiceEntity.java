package ua.shpp.eqbot.model;

import javax.persistence.*;

@Entity
@Table(name = "services")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long idTelegram;
    private String name;
    private String description;
    private byte[] avatar;

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
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

    public String getStartWorkInSunday() {
        return startWorkInSunday;
    }

    public ServiceEntity setStartWorkInSunday(String startWorkInSunday) {
        this.startWorkInSunday = startWorkInSunday;
        return this;
    }

    public String getEndWorkInSunday() {
        return endWorkInSunday;
    }

    public ServiceEntity setEndWorkInSunday(String endWorkInSunday) {
        this.endWorkInSunday = endWorkInSunday;
        return this;
    }

    public String getStartWorkInMonday() {
        return startWorkInMonday;
    }

    public ServiceEntity setStartWorkInMonday(String startWorkInMonday) {
        this.startWorkInMonday = startWorkInMonday;
        return this;
    }

    public String getEndWorkInMonday() {
        return endWorkInMonday;
    }

    public ServiceEntity setEndWorkInMonday(String endWorkInMonday) {
        this.endWorkInMonday = endWorkInMonday;
        return this;
    }

    public String getStartWorkInTuesday() {
        return startWorkInTuesday;
    }

    public ServiceEntity setStartWorkInTuesday(String startWorkInTuesday) {
        this.startWorkInTuesday = startWorkInTuesday;
        return this;
    }

    public String getEndWorkInTuesday() {
        return endWorkInTuesday;
    }

    public ServiceEntity setEndWorkInTuesday(String endWorkInTuesday) {
        this.endWorkInTuesday = endWorkInTuesday;
        return this;
    }

    public String getStartWorkInWednesday() {
        return startWorkInWednesday;
    }

    public ServiceEntity setStartWorkInWednesday(String startWorkInWednesday) {
        this.startWorkInWednesday = startWorkInWednesday;
        return this;
    }

    public String getEndWorkInwWednesday() {
        return endWorkInwWednesday;
    }

    public ServiceEntity setEndWorkInwWednesday(String endWorkInwWednesday) {
        this.endWorkInwWednesday = endWorkInwWednesday;
        return this;
    }

    public String getStartWorkInThursday() {
        return startWorkInThursday;
    }

    public ServiceEntity setStartWorkInThursday(String startWorkInThursday) {
        this.startWorkInThursday = startWorkInThursday;
        return this;
    }

    public String getEndWorkInThursday() {
        return endWorkInThursday;
    }

    public ServiceEntity setEndWorkInThursday(String endWorkInThursday) {
        this.endWorkInThursday = endWorkInThursday;
        return this;
    }

    public String getStartWorkInFriday() {
        return startWorkInFriday;
    }

    public ServiceEntity setStartWorkInFriday(String startWorkInFriday) {
        this.startWorkInFriday = startWorkInFriday;
        return this;
    }

    public String getEndWorkInFriday() {
        return endWorkInFriday;
    }

    public ServiceEntity setEndWorkInFriday(String endWorkInFriday) {
        this.endWorkInFriday = endWorkInFriday;
        return this;
    }

    public String getStartWorkInSaturday() {
        return startWorkInSaturday;
    }

    public ServiceEntity setStartWorkInSaturday(String startWorkInSaturday) {
        this.startWorkInSaturday = startWorkInSaturday;
        return this;
    }

    public String getEndWorkInSaturday() {
        return endWorkInSaturday;
    }

    public ServiceEntity setEndWorkInSaturday(String endWorkInSaturday) {
        this.endWorkInSaturday = endWorkInSaturday;
        return this;
    }

    public String getTimeBetweenClients() {
        return timeBetweenClients;
    }

    public ServiceEntity setTimeBetweenClients(String timeBetweenClients) {
        this.timeBetweenClients = timeBetweenClients;
        return this;
    }

    private String timeBetweenClients;

    public Long getId() {
        return id;
    }

    public ServiceEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getIdTelegram() {
        return idTelegram;
    }

    public ServiceEntity setIdTelegram(Long idTelegram) {
        this.idTelegram = idTelegram;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ServiceEntity setDescription(String description) {
        this.description = description;
        return this;
    }
}
