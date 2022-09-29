package ua.shpp.eqbot.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

public class ServiceDTO {
    @NotNull
    private Long id_telegram;
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

    public ServiceDTO() {
    }

    public Long getId_telegram() {
        return id_telegram;
    }

    public ServiceDTO setId_telegram(Long id_telegram) {
        this.id_telegram = id_telegram;
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
