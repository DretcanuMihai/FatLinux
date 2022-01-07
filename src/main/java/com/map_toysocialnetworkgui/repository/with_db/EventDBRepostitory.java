package com.map_toysocialnetworkgui.repository.with_db;

import com.map_toysocialnetworkgui.model.entities.Event;
import com.map_toysocialnetworkgui.repository.paging.Page;
import com.map_toysocialnetworkgui.repository.paging.Pageable;
import com.map_toysocialnetworkgui.repository.skeletons.entity_based.EventRepositoryInterface;

public class EventDBRepostitory implements EventRepositoryInterface {
    /**
     * the database's URL
     */
    private final String url;
    /**
     * the database's username
     */
    private final String username;
    /**
     * the database's password
     */
    private final String password;

    /**
     * creates a database repository with an url, a username and a password
     *
     * @param url      - url of database
     * @param username - username of database
     * @param password - password of database
     */
    public EventDBRepostitory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Event findOne(Integer integer) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        return null;
    }

    @Override
    public Event save(Event entity) {
        return null;
    }

    @Override
    public Event delete(Integer integer) {
        return null;
    }

    @Override
    public Event update(Event entity) {
        return null;
    }

    @Override
    public Iterable<Event> getUserNotificationEvents(String email) {
        return null;
    }

    @Override
    public Page<Event> getUserNotificationEvents(String email, Pageable pageable) {
        return null;
    }

    @Override
    public Iterable<Event> getUsersEventsDesc(String email) {
        return null;
    }

    @Override
    public Page<Event> getUsersEventsDesc(String email, Pageable pageable) {
        return null;
    }

    @Override
    public Iterable<Event> getEventsFilter(String string) {
        return null;
    }

    @Override
    public Page<Event> getEventsFilter(String string, Pageable pageable) {
        return null;
    }
}
