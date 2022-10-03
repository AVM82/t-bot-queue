package ua.shpp.eqbot.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ServiceRestDTO {
    @NotNull(message = "{valid.service.telegramId.notnull.message}")
    private Long telegramId;
    @Size(min = 3, max = 50, message = "{valid.service.name.size.message}")
    private String name;

    private String description;

    public Long getTelegramId() {
        return telegramId;
    }

    public ServiceRestDTO setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceRestDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ServiceRestDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
