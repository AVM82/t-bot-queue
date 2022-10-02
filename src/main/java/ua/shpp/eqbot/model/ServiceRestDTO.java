package ua.shpp.eqbot.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ServiceRestDTO {
    @NotNull(message = "{valid.service.idTelegram.notnull.message}")
    private Long idTelegram;
    @Size(min = 3, max = 50, message = "{valid.service.name.size.message}")
    private String name;

    private String description;

    public Long getIdTelegram() {
        return idTelegram;
    }

    public ServiceRestDTO setIdTelegram(Long idTelegram) {
        this.idTelegram = idTelegram;
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
