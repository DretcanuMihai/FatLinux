package com.map_toysocialnetworkgui.utils.observer;

import com.map_toysocialnetworkgui.utils.events.ObsEvent;

/**
 * observer class for observer pattern
 *
 * @param <E> - an event type
 */
public interface Observer<E extends ObsEvent> {
    /**
     * updates the Observer based on the information of the event e
     *
     * @param event - said event
     */
    void update(E event);
}
