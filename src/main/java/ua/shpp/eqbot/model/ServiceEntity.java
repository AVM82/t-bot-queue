package ua.shpp.eqbot.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "services")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramId;
    private String name;
    private String description;

    @Type(type = "org.hibernate.type.BinaryType")
    @Column(columnDefinition = "bytea")
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

    public Long getIdTelegram() {
        return idTelegram;
    }

    public ServiceEntity setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceEntity that = (ServiceEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (telegramId != null ? !telegramId.equals(that.telegramId) : that.telegramId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return Arrays.equals(avatar, that.avatar);
    }

    @Override
    public String toString() {
        return "ServiceEntity{" +
                "id=" + id +
                ", telegramId=" + telegramId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", avatar=" + Arrays.toString(avatar) +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (telegramId != null ? telegramId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }
}
