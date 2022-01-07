package com.map_toysocialnetworkgui.repository.skeletons.entity_based;

import com.map_toysocialnetworkgui.model.entities.Event;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.skeletons.CrudRepository;

public interface EventRepositoryInterface extends CrudRepository<Integer, Event> {
    /**
     * gets all events that are yet to have passed for a user
     *
     * @param email -> said user's email
     * @return an iterable of said events
     */
    Iterable<Event> getUserNotificationEvents(String email);

    /**
     * gets a page of events that are yet to have passed for a user
     *
     * @param email -> said user's email
     * @return a page of said events
     */
    Page<Event> getUserNotificationEvents(String email, Pageable pageable);

    /**
     * gets all events for which user is attendee
     *
     * @param email -> said user's email
     * @return an iterable of said events
     */
    Iterable<Event> getUsersEventsDesc(String email);

    /**
     * gets all events for which user is attendee
     *
     * @param email -> said user's email
     * @return a page of said events
     */
    Page<Event> getUsersEventsDesc(String email, Pageable pageable);

    /**
     * gets all events which have a string in the title or description
     *
     * @param string -> said string
     * @return an iterable of said events
     */
    Iterable<Event> getEventsFilter(String string);

    /**
     * gets a page of all events which have a string in the title or description
     *
     * @param string -> said string
     * @return a page of said events
     */
    Page<Event> getEventsFilter(String string, Pageable pageable);
}
