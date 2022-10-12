package ua.shpp.eqbot.paging;

public class Pair {
    private int page;
    private int size;

    public Pair(int from, int size) {
        this.page = from;
        this.size = size;
    }

    public int getFrom() {
        return page;
    }

    public Pair setFrom(int from) {
        this.page = from;
        return this;
    }

    public int getSize() {
        return size;
    }

    public Pair setSize(int size) {
        this.size = size;
        return this;
    }

    public Pair increase() {
        this.from += 2;
        this.to += 2;
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
        if (size != pair.size) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + size;
        return result;
    }
}
