package ua.shpp.eqbot.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ServiceDTO {
    @NotNull
    private Long idTelegram;
    @Min(3)
    private String name;

    private String description;

    public MenuTimeWork getMenuTimeWork() {
        return menuTimeWork;
    }

    public ServiceDTO setMenuTimeWork(MenuTimeWork menuTimeWork) {
        this.menuTimeWork = menuTimeWork;
        return this;
    }

    private MenuTimeWork menuTimeWork;

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
