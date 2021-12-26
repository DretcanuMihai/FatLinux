package com.map_toysocialnetworkgui.utils.observer;


import com.map_toysocialnetworkgui.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
