package com.map_toysocialnetworkgui.utils.observer;

import com.map_toysocialnetworkgui.utils.events.Event;

/**
 * observable class for observer pattern
 *
 * @param <E> - event type
 */
public interface Observable<E extends Event> {
    /**
     * adds observer for notification
     *
     * @param observer - said observer
     */
    void addObserver(Observer<E> observer);

    /**
     * removes an observer from notification
     *
     * @param observer - said observer
     */
    void removeObserver(Observer<E> observer);

    /**
     * notifies observers with an event
     *
     * @param event - said event
     */
    void notifyObservers(E event);
}
