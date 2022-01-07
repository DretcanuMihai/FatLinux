package com.map_toysocialnetworkgui.model.entities;

import com.map_toysocialnetworkgui.model.entities_dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public class Event {
    Integer id;
    String title;
    String description;
    String hostEmail;
    List<String> attendees;
    LocalDateTime date;

    /**
     * constructs event dto by needed information
     * @param id - event id
     * @param title - event title
     * @param description - event description
     * @param hostEmail - event host
     * @param attendees - event attendees
     * @param date - event date
     */
    public Event(Integer id, String title, String description, String hostEmail, List<String> attendees, LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.hostEmail = hostEmail;
        this.attendees = attendees;
        this.date = date;
    }

    /**
     * gets event id;
     * @return said id
     */
    public Integer getId() {
        return id;
    }

    /**
     * gets title
     * @return said title
     */
    public String getTitle() {
        return title;
    }

    /**
     * gets description
     * @return said description
     */
    public String getDescription() {
        return description;
    }

    /**
     * gets host user email
     * @return said email
     */
    public String getHostEmail() {
        return hostEmail;
    }

    /**
     * attendee email list
     * @return said list
     */
    public List<String> getAttendees() {
        return attendees;
    }

    /**
     * gets date
     *
     * @return said date
     */
    public LocalDateTime getDate() {
        return date;
    }
}
