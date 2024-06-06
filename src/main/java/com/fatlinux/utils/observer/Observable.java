package com.fatlinux.utils.observer;

import com.fatlinux.utils.events.ObsEvent;

/**
 * observable class for observer pattern
 *
 * @param <E> - event type
 */
public interface Observable<E extends ObsEvent> {
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
