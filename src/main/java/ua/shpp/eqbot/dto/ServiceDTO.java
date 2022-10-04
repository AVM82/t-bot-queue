package ua.shpp.eqbot.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ServiceDTO {
    @NotNull(message = "{valid.service.telegramId.notnull.message}")
    private Long telegramId;
    @Size(min = 3, max = 50, message = "{valid.service.name.size.message}")
    private String name;

    private String description;

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
