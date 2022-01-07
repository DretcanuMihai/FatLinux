package com.map_toysocialnetworkgui.model.validators;

import com.map_toysocialnetworkgui.model.entities.Event;

import java.time.LocalDateTime;

public class EventValidator implements Validator<Event>{

    static private final int maxVarcharSize = 50;

    @Override
    public void validateDefault(Event entity) throws ValidationException {
        String message = "";
        String title=entity.getTitle();
        String description=entity.getDescription();
        LocalDateTime date=entity.getDate();
        if (title == null || title.equals("") || title.length() > maxVarcharSize)
            message += "Invalid event title! Must be non null and not have more than " + maxVarcharSize + " characters;\n";
        if (description == null || description.equals("") || description.length() > maxVarcharSize)
            message += "Invalid event description! Must be non null and not have more than " + maxVarcharSize + " characters;\n";
        if(date==null || date.compareTo(LocalDateTime.now())>0)
            message += "Invalid event date! Must be non null and not have passed";
        if (message.length() != 0) {
            throw new ValidationException(message);
        }
    }
}
