package ua.shpp.eqbot.cache;

import java.util.List;

public interface Cache <T>{
    //додати користувача
    void add(T t);
    //видалити рористувача
    void remove(T t);
    //пошук користувача
    T findBy(Long id);
    //знайти всіх користувачів
    List<T> getAll();
}
