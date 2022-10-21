package ua.shpp.eqbot.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "services")
public class ServiceEntity {

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "serviceEntity")
    Set<RegistrationForTheServiceEntity> registrationForTheServiceEntities;
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

    public ServiceEntity setSundayWorkingHours(String sundayWorkingHours) {
        this.sundayWorkingHours = sundayWorkingHours;
        return this;
    }

    public String getMondayWorkingHours() {
        return mondayWorkingHours;
    }

    public ServiceEntity setMondayWorkingHours(String mondayWorkingHours) {
        this.mondayWorkingHours = mondayWorkingHours;
        return this;
    }

    public String getTuesdayWorkingHours() {
        return tuesdayWorkingHours;
    }

    public ServiceEntity setTuesdayWorkingHours(String tuesdayWorkingHours) {
        this.tuesdayWorkingHours = tuesdayWorkingHours;
        return this;
    }

    public String getWednesdayWorkingHours() {
        return wednesdayWorkingHours;
    }

    public ServiceEntity setWednesdayWorkingHours(String wednesdayWorkingHours) {
        this.wednesdayWorkingHours = wednesdayWorkingHours;
        return this;
    }

    public String getThursdayWorkingHours() {
        return thursdayWorkingHours;
    }

    public ServiceEntity setThursdayWorkingHours(String startWorkInThursday) {
        this.thursdayWorkingHours = startWorkInThursday;
        return this;
    }

    public String getFridayWorkingHours() {
        return fridayWorkingHours;
    }

    public ServiceEntity setFridayWorkingHours(String startWorkInFriday) {
        this.fridayWorkingHours = startWorkInFriday;
        return this;
    }

    public String getSaturdayWorkingHours() {
        return saturdayWorkingHours;
    }

    public ServiceEntity setSaturdayWorkingHours(String startWorkInSaturday) {
        this.saturdayWorkingHours = startWorkInSaturday;
        return this;
    }

    public String getTimeBetweenClients() {
        return timeBetweenClients;
    }

    public ServiceEntity setTimeBetweenClients(String timeBetweenClients) {
        this.timeBetweenClients = timeBetweenClients;
        return this;
    }

    public Long getTelegramId() {
        return telegramId;
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
        if (this == o) {
            return true;
        }
        if (o == null
                || getClass() != o.getClass()) {
            return false;
        }

        ServiceEntity that = (ServiceEntity) o;

        if (!Objects.equals(id, that.id)) {
            return false;
        }
        if (!Objects.equals(telegramId, that.telegramId)) {
            return false;
        }
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        if (!Objects.equals(description, that.description)) {
            return false;
        }
        return Arrays.equals(avatar, that.avatar);
    }

    @Override
    public String toString() {
        return "ServiceEntity{"
                + "id=" + id
                + ", telegramId=" + telegramId
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", avatar=" + Arrays.toString(avatar)
                + '}';
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

    public Set<RegistrationForTheServiceEntity> getRegistrationForTheServiceEntities() {
        return registrationForTheServiceEntities;
    }

    public void setRegistrationForTheServiceEntities(Set<RegistrationForTheServiceEntity> registrationForTheServiceEntities) {
        this.registrationForTheServiceEntities = registrationForTheServiceEntities;
    }
}
