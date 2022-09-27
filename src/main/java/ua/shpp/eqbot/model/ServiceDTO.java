package ua.shpp.eqbot.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

public class ServiceDTO {
    @NotNull
    private Long idTelegram;
    @Min(3)
    private String name;
    @DefaultValue("Опис")
    private String description;

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
