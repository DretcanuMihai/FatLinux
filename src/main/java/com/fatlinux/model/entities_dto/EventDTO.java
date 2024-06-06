package com.fatlinux.model.entities_dto;

import com.fatlinux.model.entities.Event;
import com.fatlinux.utils.structures.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class EventDTO {
    Integer id;
    String title;
    String description;
    UserDTO hostUser;
    List<Pair<String,Boolean>> attendees;
    LocalDateTime date;

    /**
     * constructs event dto by needed information
     * @param id - event id
     * @param title - event title
     * @param description - event description
     * @param hostUser - event host
     * @param attendees - event attendees
     * @param date - event date
     */
    public EventDTO(Integer id, String title, String description, UserDTO hostUser, List<Pair<String,Boolean>> attendees, LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.hostUser = hostUser;
        this.attendees = attendees;
        this.date = date;
    }

    public EventDTO(Event event,UserDTO userDTO){
        this(event.getId(),event.getTitle(),event.getDescription(),userDTO,event.getAttendees(),event.getDate());
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
     * gets host user dto
     * @return said dto
     */
    public UserDTO getHostUser() {
        return hostUser;
    }

    /**
     * attendee email list
     * @return said list
     */
    public List<Pair<String,Boolean>> getAttendees() {
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
