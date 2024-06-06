package com.fatlinux.repository.skeletons.entity_based;

import com.fatlinux.model.entities.Event;
import com.fatlinux.model.entities.User;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.paging.PagingRepository;

public interface EventRepositoryInterface extends PagingRepository<Integer, Event> {
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

    /**
     * gets the number of notifications of a user
     * @param user - said user
     * @return said number
     */
    int getNumberOfNotification(User user);
}
