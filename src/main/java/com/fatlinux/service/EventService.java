package com.fatlinux.service;

import com.fatlinux.model.entities.Event;
import com.fatlinux.model.entities.User;
import com.fatlinux.model.validators.EventValidator;
import com.fatlinux.model.validators.ValidationException;
import com.fatlinux.repository.paging.Page;
import com.fatlinux.repository.paging.Pageable;
import com.fatlinux.repository.skeletons.entity_based.EventRepositoryInterface;
import com.fatlinux.utils.structures.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class EventService {
    EventValidator validator;
    EventRepositoryInterface repo;

    /**
     * creates a friend request service based on a friendship repository and validator and
     * friend request repository and validator
     *
     * @param repo      - said event repo repository
     * @param validator - said event validator
     */
    public EventService(EventRepositoryInterface repo, EventValidator validator) {
        this.validator = validator;
        this.repo = repo;
    }

    /**
     * returns an event identified by an id
     *
     * @param id - said id
     * @return - said event
     * @throws ValidationException     if id is null
     * @throws AdministrationException if no event exists with given id
     */
    public Event findOne(Integer id) throws ValidationException, AdministrationException {
        validator.validateId(id);
        Event event = repo.findOne(id);
        if (event == null)
            throw new AdministrationException("Error: No event with given id;\n");
        return event;
    }

    /**
     * saves an event with no participants
     *
     * @param title       - said event's title
     * @param description - said event's description
     * @param hostEmail   - said event's host's email
     * @param date        - said event's date
     * @throws ValidationException is data is invalid
     */
    public void save(String title, String description, String hostEmail, LocalDateTime date) throws ValidationException {
        Event event = new Event(null, title, description, hostEmail, List.of(new Pair<>(hostEmail,true)), date);
        validator.validateDefault(event);
        repo.save(event);
    }

    /**
     * deletes event identified by an id
     *
     * @param id - said id
     * @throws ValidationException     if id is null
     * @throws AdministrationException if no event exists with given id
     */
    public void delete(Integer id) throws ValidationException, AdministrationException {
        validator.validateId(id);
        Event returnValue = repo.delete(id);
        if (returnValue == null)
            throw new AdministrationException("Error: No event with given id;\n");
    }

    /**
     * subscribes an user to an event
     *
     * @param id        - said event's id
     * @param userEmail - said user's emails
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if the event doesn't exist or it's already subscribed
     */
    public void subscribeToEvent(Integer id, String userEmail) throws ValidationException, AdministrationException {
        Event event = findOne(id);
        for(Pair<String,Boolean> p : event.getAttendees()) {
            if(p.getFirst().equals(userEmail))
                throw new AdministrationException("Error: User already subscribed to event;\n");
        }
        event.getAttendees().add(new Pair<>(userEmail,true));
        repo.update(event);
    }

    /**
     * unsubscribes an user to an event
     *
     * @param id        - said event's id
     * @param userEmail - said user's emails
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if the event doesn't exist or it's not subscribed
     */
    public void unsubscribeFromEvent(Integer id, String userEmail) {
        Event event = findOne(id);
        Pair<String,Boolean> toDelete=null;
        for(Pair<String,Boolean> p : event.getAttendees()) {
            if(p.getFirst().equals(userEmail))
                toDelete=p;
        }
        if(toDelete==null)
            throw new AdministrationException("Error: User isn't subscribed to event;\n");
        event.getAttendees().remove(toDelete);
        repo.update(event);
    }

    /**
     * subscribes an user to an event's notifications
     *
     * @param id        - said event's id
     * @param userEmail - said user's emails
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if the event doesn't exist or it's already subscribed
     */
    public void requestNotificationsFromEvent(Integer id, String userEmail) throws ValidationException, AdministrationException {
        Event event = findOne(id);
        Pair<String,Boolean> toModify=null;
        for(Pair<String,Boolean> p : event.getAttendees()) {
            if(p.getFirst().equals(userEmail))
                toModify=p;
        }
        if(toModify==null){
            throw new AdministrationException("Error: User isn't subscribed to event;\n");
        }
        if(toModify.getSecond().equals(true))
            throw new AdministrationException("Error: User already receives notifications from event;\n");
        event.getAttendees().remove(toModify);
        event.getAttendees().add(new Pair<>(userEmail,true));
        repo.update(event);
    }

    /**
     * unsubscribes an user to an event's notifications
     *
     * @param id        - said event's id
     * @param userEmail - said user's emails
     * @throws ValidationException     - if data is invalid
     * @throws AdministrationException - if the event doesn't exist or it's not subscribed
     */
    public void unrequestNotificationsFromEvent(Integer id, String userEmail) {
        Event event = findOne(id);
        Pair<String,Boolean> toModify=null;
        for(Pair<String,Boolean> p : event.getAttendees()) {
            if(p.getFirst().equals(userEmail))
                toModify=p;
        }
        if(toModify==null){
            throw new AdministrationException("Error: User isn't subscribed to event;\n");
        }
        if(toModify.getSecond().equals(false))
            throw new AdministrationException("Error: User already doesn't receive notifications from event;\n");
        event.getAttendees().remove(toModify);
        event.getAttendees().add(new Pair<>(userEmail,false));
        repo.update(event);
    }

    /**
     * gets a page of all user notification events
     *
     * @param email    - said user's email
     * @param pageable - paging info
     * @return - said page
     */
    public Page<Event> getUserNotificationEvents(String email, Pageable pageable) {
        return repo.getUserNotificationEvents(email, pageable);
    }

    /**
     * gets a page of all user events
     *
     * @param email    - said user's email
     * @param pageable - paging info
     * @return - said page
     */
    public Page<Event> getUsersEventsDesc(String email, Pageable pageable) {
        return repo.getUsersEventsDesc(email, pageable);
    }

    /**
     * gets a page of all events described by a string
     *
     * @param string   - said string
     * @param pageable - paging info
     * @return - said page
     * @throws ValidationException - if string is null or empty
     */
    public Page<Event> getEventsFilter(String string, Pageable pageable) {
        if (string == null || string.equals(""))
            throw new ValidationException("Error:String must be not null and not empty");
        return repo.getEventsFilter(string, pageable);
    }

    public int getNumberOfNotification(User user) {
        return repo.getNumberOfNotification(user);
    }
}
