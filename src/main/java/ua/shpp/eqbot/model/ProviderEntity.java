package ua.shpp.eqbot.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "provider_entity")
public class ProviderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramId;

    @ElementCollection
    private HashSet<Long> blackList;
    private String name;
    private String providerCity;

    public HashSet<Long> getBlackList() {
        return blackList;
    }

    public void setBlackList(HashSet<Long> blackList) {
        this.blackList = blackList;
    }

    public ProviderEntity() {
    }

    public Long getId() {
        return id;
    }

    public ProviderEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public ProviderEntity setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProviderEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getProviderCity() {
        return providerCity;
    }

    public ProviderEntity setProviderCity(String providerCity) {
        this.providerCity = providerCity;
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

        ProviderEntity that = (ProviderEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProviderEntity{"
                + "id=" + id
                + ", telegramId=" + telegramId
                + ", name='" + name + '\''
                + ", providerCity='" + providerCity + '\''
                + '}';
    }
}
