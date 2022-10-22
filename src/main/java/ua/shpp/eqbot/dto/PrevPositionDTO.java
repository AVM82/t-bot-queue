package ua.shpp.eqbot.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import ua.shpp.eqbot.stage.PositionMenu;

import java.util.Objects;

public class PrevPositionDTO {


    Long telegramId;
    PositionMenu positionMenu;
    String receivedData;
    int page = 0;


    public PrevPositionDTO(Long telegramId, PositionMenu positionMenu, String receivedData) {
        this.telegramId = telegramId;
        this.positionMenu = positionMenu;
        this.receivedData = receivedData;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public PrevPositionDTO() {
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public PositionMenu getPositionMenu() {
        return positionMenu;
    }

    public void setPositionMenu(PositionMenu positionMenu) {
        this.positionMenu = positionMenu;
    }

    public String getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(String receivedData) {
        this.receivedData = receivedData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrevPositionDTO that = (PrevPositionDTO) o;

        if (page != that.page) {
            return false;
        }

        if (!Objects.equals(telegramId, that.telegramId)) {
            return false;
        }
        if (positionMenu != that.positionMenu) {
            return false;
        }
        return Objects.equals(receivedData, that.receivedData);
    }

    @Override
    public int hashCode() {
        int result = telegramId != null ? telegramId.hashCode() : 0;
        result = 31 * result + (positionMenu != null ? positionMenu.hashCode() : 0);
        result = 31 * result + (receivedData != null ? receivedData.hashCode() : 0);
        result = 31 * result + page;
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("telegramId", telegramId)
                .append("positionMenu", positionMenu)
                .append("receivedData", receivedData)
                .append("page", page)
                .toString();
    }
}
