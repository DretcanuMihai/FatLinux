package com.map_toysocialnetworkgui.service;

import com.map_toysocialnetworkgui.model.entities.Event;
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

    public Event findOne(Integer id) throws IllegalArgumentException {
        validator.validateId(id);
        Event event=repo.findOne(id);
        if(event==null)
            throw new AdministrationException("Error: No event with given id;\n");
        return event;
    }

    public void save(String title, String description, String hostEmail, LocalDateTime date) {
        Event event=new Event(null,title,description,hostEmail,new ArrayList<>(),date);
        repo.save(event);
    }

    public void delete(Integer integer) {
        validator.validateId(integer);
        Event returnValue=repo.delete(integer);
        if(returnValue==null)
            throw new AdministrationException("Error: No event with given id;\n");
    }

    public Page<Event> getUserNotificationEvents(String email, Pageable pageable) {
        return repo.getUserNotificationEvents(email, pageable);
    }

    public Page<Event> getUsersEventsDesc(String email, Pageable pageable) {
        return repo.getUsersEventsDesc(email, pageable);
    }

    public Page<Event> getEventsFilter(String string, Pageable pageable) {
        if(string==null || string.equals(""))
            throw new ValidationException("Error:String must be not null and not empty");
        return repo.getEventsFilter(string, pageable);
    }
}
