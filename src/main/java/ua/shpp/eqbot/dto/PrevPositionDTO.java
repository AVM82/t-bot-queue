package ua.shpp.eqbot.dto;

import ua.shpp.eqbot.stage.PositionMenu;

public class PrevPositionDTO {

    Long telegramId;
    PositionMenu positionMenu;
    String receivedData;
    int page = -1;
    int pageSize = -1;

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrevPositionDTO that = (PrevPositionDTO) o;

        if (page != that.page) return false;
        if (pageSize != that.pageSize) return false;
        if (telegramId != null ? !telegramId.equals(that.telegramId) : that.telegramId != null) return false;
        if (positionMenu != that.positionMenu) return false;
        return receivedData != null ? receivedData.equals(that.receivedData) : that.receivedData == null;
    }

    @Override
    public int hashCode() {
        int result = telegramId != null ? telegramId.hashCode() : 0;
        result = 31 * result + (positionMenu != null ? positionMenu.hashCode() : 0);
        result = 31 * result + (receivedData != null ? receivedData.hashCode() : 0);
        result = 31 * result + page;
        result = 31 * result + pageSize;
        return result;
    }

    @Override
    public String toString() {
        return "PrevPositionDTO{" +
                "telegramId=" + telegramId +
                ", positionMenu=" + positionMenu +
                ", receivedData='" + receivedData + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
