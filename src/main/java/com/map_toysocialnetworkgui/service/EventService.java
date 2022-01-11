package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.Event;
import com.map_toysocialnetworkgui.model.entities.User;
import com.map_toysocialnetworkgui.model.validators.EventValidator;
import com.map_toysocialnetworkgui.model.validators.ValidationException;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.EventRepositoryInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EventService {
    EventValidator validator;
    EventRepositoryInterface repo;

    /**
     * creates a friend request service based on a friendship repository and validator and
     * friend request repository and validator
     *
     * @param repo - said event repo repository
     * @param validator  - said event validator
     */
    public EventService(EventRepositoryInterface repo,EventValidator validator) {
        this.validator = validator;
        this.repo = repo;
    }

    /**
     * returns an event identified by an id
     * @param id - said id
     * @return - said event
     * @throws ValidationException if id is null
     * @throws AdministrationException if no event exists with given id
     */
    public Event findOne(Integer id) throws ValidationException,AdministrationException {
        validator.validateId(id);
        Event event=repo.findOne(id);
        if(event==null)
            throw new AdministrationException("Error: No event with given id;\n");
        return event;
    }

    /**
     * saves an event with no participants
     *
     * @param title - said event's title
     * @param description - said event's description
     * @param hostEmail - said event's host's email
     * @param date - said event's date
     * @throws ValidationException is data is invalid
     */
    public void save(String title, String description, String hostEmail, LocalDateTime date)throws ValidationException {
        Event event=new Event(null,title,description,hostEmail,new ArrayList<>(),date);
        repo.save(event);
    }

    /**
     * deletes event identified by an id
     * @param id - said id
     *
     * @throws ValidationException if id is null
     * @throws AdministrationException if no event exists with given id
     */
    public void delete(Integer id) throws ValidationException,AdministrationException{
        validator.validateId(id);
        Event returnValue=repo.delete(id);
        if(returnValue==null)
            throw new AdministrationException("Error: No event with given id;\n");
    }

    /**
     * subscribes an user to an event
     * @param id - said event's id
     * @param userEmail - said user's emails
     * @throws ValidationException - if data is invalid
     * @throws AdministrationException - if the event doesn't exist or it's already subscribed
     */
    public void subscribeToEvent(Integer id, String userEmail)throws ValidationException,AdministrationException{
        Event event=findOne(id);
        if(event.getAttendees().contains(userEmail))
            throw new AdministrationException("Error: User already subscribed to event;\n");
        event.getAttendees().add(userEmail);
        repo.update(event);
    }

    /**
     * unsubscribes an user to an event
     * @param id - said event's id
     * @param userEmail - said user's emails
     * @throws ValidationException - if data is invalid
     * @throws AdministrationException - if the event doesn't exist or it's not subscribed
     */
    public void unsubscribeFromEvent(Integer id,String userEmail){
        Event event=findOne(id);
        if(!event.getAttendees().contains(userEmail))
            throw new AdministrationException("Error: User not subscribed to event;\n");
        event.getAttendees().remove(userEmail);
        repo.update(event);
    }

    /**
     * gets a page of all user notification events
     * @param email - said user's email
     * @param pageable - paging info
     * @return - said page
     */
    public Page<Event> getUserNotificationEvents(String email, Pageable pageable) {
        return repo.getUserNotificationEvents(email, pageable);
    }

    /**
     * gets a page of all user events
     * @param email - said user's email
     * @param pageable - paging info
     * @return - said page
     */
    public Page<Event> getUsersEventsDesc(String email, Pageable pageable) {
        return repo.getUsersEventsDesc(email, pageable);
    }

    /**
     * gets a page of all events described by a string
     * @param string - said string
     * @param pageable - paging info
     * @return - said page
     * @throws ValidationException - if string is null or empty
     */
    public Page<Event> getEventsFilter(String string, Pageable pageable) {
        if(string==null || string.equals(""))
            throw new ValidationException("Error:String must be not null and not empty");
        return repo.getEventsFilter(string, pageable);
    }

    public int getNumberOfNotification(User user) {
        return repo.getNumberOfNotification(user);
    }
}
