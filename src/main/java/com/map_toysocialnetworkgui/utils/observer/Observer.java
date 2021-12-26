package com.map_toysocialnetworkgui.utils.observer;


import com.map_toysocialnetworkgui.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}