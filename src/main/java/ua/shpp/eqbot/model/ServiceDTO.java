package ua.shpp.eqbot.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ServiceDTO {
    @NotNull(message = "{valid.service.idTelegram.notnull.message}")
    private Long idTelegram;
    @Size(min = 3, max = 50, message = "{valid.service.name.size.message}")
    private String name;

    private String description;

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
