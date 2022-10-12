package ua.shpp.eqbot.paging;

public class Pair {
    private int from;
    private int to;

    public Pair(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public Pair setFrom(int from) {
        this.from = from;
        return this;
    }

    public int getTo() {
        return to;
    }

    public Pair setTo(int to) {
        this.to = to;
        return this;
    }

    public Pair increase() {
        this.from += 2;
        this.to += 2;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (from != pair.from) return false;
        if (to != pair.to) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        return result;
    }
}
