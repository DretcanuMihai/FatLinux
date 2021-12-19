package com.map_toysocialnetworkgui.model.validators;

import com.map_toysocialnetworkgui.model.entities.User;

import java.util.regex.Pattern;

/**
 * user entity validator
 */
public class UserValidator implements Validator<User>{
    static private final Pattern CSVPattern =Pattern.compile("^.*,.*$");
    static private final Pattern emailPattern =Pattern.compile("^[^@ ]+@[^@ ]+$");
    static private final int maxSize=50;

    /**
     * validates the user entity if:
     * ->its email is non-null, non-empty, doesn't have commas and matches the email pattern
     * ->its first name and last name are non-null, non-empty and don't have commas
     * ->its passwordHash is not 0
     * @param entity - said entity
     * @throws ValidationException - if said entity is not valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String message="";
        String email=entity.getEmail();
        String firstName=entity.getFirstName();
        String lastName = entity.getLastName();
        int passwordHash=entity.getPasswordHash();
        if(email==null || email.equals("")|| CSVPattern.matcher(email).matches()
                || !emailPattern.matcher(email).matches() || email.length()>maxSize)
            message+="Invalid email! Must be non null and non empty;\n";
        if(firstName==null ||firstName.equals("") || CSVPattern.matcher(firstName).matches()||
                firstName.length()>maxSize)
            message += "Invalid first name! Must be non null and non empty;\n";
        if(lastName==null || lastName.equals("") || CSVPattern.matcher(lastName).matches() ||
                lastName.length() > maxSize)
            message += "Invalid last name! Must be non null and non empty;\n";
        if(passwordHash==0)
            message+="Invalid password hash! Must be non 0;\n";
        if(!message.equals(""))
            throw new ValidationException(message);
    }

    public void validateEmail(String email)throws ValidationException {
        if(email==null || email.equals("")|| CSVPattern.matcher(email).matches()
                || !emailPattern.matcher(email).matches()|| email.length()>maxSize)
            throw new ValidationException("Invalid email! Must be non null and non empty;\n");
    }
}
