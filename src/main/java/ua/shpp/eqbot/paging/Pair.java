package ua.shpp.eqbot.paging;

import org.springframework.beans.factory.annotation.Value;
import ua.shpp.eqbot.model.ServiceEntity;

import java.util.List;

public class Pair {
    private int page;
    /*TODO help why does not take the file from the property*/
    @Value("${paging.size:3}")
    private int size = 2;
    private boolean isLast;
    private List<ServiceEntity> serviceEntities;
    public Pair(int from) {
        this.page = from;
    }

    public int getFrom() {
        return page;
    }

    public Pair setFrom(int from) {
        this.page = from;
        return this;
    }

    public List<ServiceEntity> getServiceEntities() {
        return serviceEntities;
    }

    public Pair setServiceEntities(List<ServiceEntity> serviceEntities) {
        this.serviceEntities = serviceEntities;
        return this;
    }

    public Pair increase() {
        this.page += 1;
        return this;
    }

    public Pair decrease() {
        this.page -= 1;
        if (this.page <= 0) {
            this.page = 0;
        }
        return this;
    }

    public boolean isLast() {
        return isLast;
    }

    public Pair setLast(boolean last) {
        isLast = last;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair pair = (Pair) o;
        if (page != pair.page) {
            return false;
        }
        return size == pair.size;
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + size;
        return result;
    }

    public int getSize() {
        return size;
    }
}
