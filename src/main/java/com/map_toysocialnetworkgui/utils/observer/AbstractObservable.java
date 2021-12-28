package com.map_toysocialnetworkgui.utils.observer;

import com.map_toysocialnetworkgui.utils.events.Event;

import java.util.ArrayList;
import java.util.Collection;

public class AbstractObservable<E extends Event> implements Observable<E> {

    Collection<Observer<E>> observerList;

    public AbstractObservable() {
        observerList=new ArrayList<>();
    }

    @Override
    public void addObserver(Observer<E> observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer<E> observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers(E event) {
        observerList.forEach(observer->observer.update(event));
    }
}
