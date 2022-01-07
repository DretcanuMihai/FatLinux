package com.map_toysocialnetworkgui.utils.observer;

import com.map_toysocialnetworkgui.utils.events.ObsEvent;

import java.util.ArrayList;
import java.util.Collection;

public class AbstractObservable<E extends ObsEvent> implements Observable<E> {
    Collection<Observer<E>> observerList;

    /**
     * creates an array list of observers for the observable entity
     */
    public AbstractObservable() {
        observerList = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer<E> observer) {
        if (!observerList.contains(observer))
            observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer<E> observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers(E event) {
        observerList.forEach(observer -> observer.update(event));
    }
}
