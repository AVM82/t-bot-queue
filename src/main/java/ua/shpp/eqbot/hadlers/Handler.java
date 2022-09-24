package ua.shpp.eqbot.hadlers;

public interface Handler<T> {
    void choose(T t);
}
