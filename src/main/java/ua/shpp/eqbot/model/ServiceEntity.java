package ua.shpp.eqbot.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "services")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long id_telegram;
    private String name;
    private String description;


    private byte[] avatar;

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public ServiceEntity() {
    }

    public Long getId() {
        return id;
    }

    public ServiceEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getId_telegram() {
        return id_telegram;
    }

    public ServiceEntity setId_telegram(Long id_telegram) {
        this.id_telegram = id_telegram;
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
